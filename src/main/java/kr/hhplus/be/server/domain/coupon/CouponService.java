package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.common.lock.aop.DistributedLockTransaction;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedRequestMessage;
import kr.hhplus.be.server.infrastructure.coupon.CouponMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CouponService {

	private final CouponRepository couponRepository;
	private final CouponMessageProducer producer;

	private static final String ISSUE_LUA =
		"if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then return -1 end\n" +
			"local remain = tonumber(redis.call('GET', KEYS[1]) or '0')\n" +
			"if remain <= 0 then return 0 end\n" +
			"redis.call('DECR', KEYS[1])\n" +
			"redis.call('SADD', KEYS[2], ARGV[1])\n" +
			"return 1";

	public void loadFcFsCoupon(Long couponId) {
		Coupon coupon = couponRepository.findCouponById(couponId).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));//RDB

		String couponKey = "coupon:" + coupon.getId() + ":remaining";
		couponRepository.setString(couponKey, String.valueOf(coupon.getQuantity()));
		couponRepository.expire(couponKey, Duration.ofHours(1));
	}

	// 쿠폰 등록
	@Transactional
	public CouponInfo.Coupon register(CouponCommand.Create command) {
		Coupon coupon = Coupon.create(
			command.getName(),
			command.getDiscountPrice(),
			command.getQuantity(),
			command.getCouponType() ,
			command.getUseStartDate(),
			command.getExpiredDate()
		);

		Coupon savedCoupon = couponRepository.saveCoupon(coupon);

		return CouponInfo.Coupon.of(
				savedCoupon.getId(),
				savedCoupon.getName(),
				savedCoupon.getDiscountPrice(),
				savedCoupon.getUseStartDate(),
				savedCoupon.getExpiredDate(),
				savedCoupon.getQuantity(),
				savedCoupon.getCouponType()
		);
	}

	public Coupon findCouponById(long couponId) {
		return couponRepository.findCouponById(couponId).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));
	}

	public void requestIssuedCoupon(RequestIssuedCouponCommand command) {
		producer.send(new CouponIssuedRequestMessage(command.getUserId(),command.getCouponId()));
	}

	@Transactional
	public Coupon issueCoupon(UserCouponCommand.Issue command) {

		Coupon findCoupon = couponRepository.findCouponById(command.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		List<UserCoupon> userCouponIds = couponRepository.findUserCouponByUserId(command.getUserId());

		// 쿠폰을 이미 가지고있는지 검사
		boolean hasCoupon = userCouponIds.stream().anyMatch(userCoupon -> userCoupon.isSameCoupon(command.getCouponId()));

		if (hasCoupon) {
			throw new GlobalBusinessException(ErrorCode.ALREADY_ISSUED_COUPON);
		}

		findCoupon.issue();

		UserCoupon userCoupon = UserCoupon.createIssuedCoupon(command.getUserId(), command.getCouponId());

		couponRepository.saveUserCoupon(userCoupon);

		return findCoupon;
	}

	// Redis String 카운터 활용
	@Transactional
	public void issueCouponWithRedisCounter(UserCouponCommand.Issue command) {
		log.info("캐시 쿠폰발급 시작");
		String couponKey = "coupon:" + command.getCouponId() + ":remaining";
		String issuedKey = "coupon:" + command.getCouponId() + ":issued";

		Long added = couponRepository.addSet(issuedKey, String.valueOf(command.getUserId()));

		if(added == 0) {
			log.info("쿠폰 발급 실패, 이미 발급된 쿠폰입니다.");
			return;
		}

		if(Integer.parseInt(couponRepository.getString(couponKey)) <= 0) {
			log.info("쿠폰 발급 실패, 쿠폰이 없습니다.");
			return;
		}

		couponRepository.decrString(couponKey);
		// 비동기 이벤트로 UserCoupon 데이터 저장
	}

	// 루아스크립트 활용
	@Transactional
	public void issueCouponWithLuaScript(UserCouponCommand.Issue command) {
		String remKey   = "coupon:" + command.getCouponId() + ":remaining";
		String issuedKey= "coupon:" + command.getCouponId() + ":issued";

		DefaultRedisScript<Long> script = new DefaultRedisScript<>();
		script.setScriptText(ISSUE_LUA);
		script.setResultType(Long.class);

		Long result = couponRepository.execute(
			script,
			List.of(remKey, issuedKey),
			String.valueOf(command.getUserId())
		);

		if (result == null || result == 0) {
			throw new GlobalBusinessException("쿠폰이 없습니다.");
		}
		if (result == -1) {
			// 이미 발급된 유저라면 그냥 리턴하거나 예외 던지기
		}

		// 여기서는 Redis에서 이미 발급 이력까지 기록했으니,
		// 필요한 비동기 DB 저장 로직만 실행합니다.
		// 예) eventPublisher.publish(new CouponIssuedEvent(...));
	}


	// 비관적락
	/**
	 * 비관적락이 걸려있는 쿠폰 발급
	 * @param command
	 * @return
	 */
	@Transactional
	public Coupon issueCouponWithPessimisticLock(UserCouponCommand.Issue command) {

		// 비관적락 조회
		Coupon findCoupon = couponRepository.findByIdUpdate(command.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		List<UserCoupon> userCouponIds = couponRepository.findUserCouponByUserId(command.getUserId());

		// 첫번째 요청 통과
		// 쿠폰을 이미 가지고있는지 검사
		boolean hasCoupon = userCouponIds.stream().anyMatch(userCoupon -> userCoupon.isSameCoupon(command.getCouponId()));

		if (hasCoupon) {
			throw new GlobalBusinessException(ErrorCode.ALREADY_ISSUED_COUPON);
		}

		UserCoupon userCoupon = UserCoupon.createIssuedCoupon(command.getUserId(), command.getCouponId());
		couponRepository.saveUserCoupon(userCoupon);

		findCoupon.issue();

		return findCoupon;
	}

	// 분산락
	@DistributedLockTransaction(key = "#lockName.concat(':').concat(#command.getCouponId())")
	public Coupon issueCouponWithAnnoationLock(String lockName, UserCouponCommand.Issue command) {
		Coupon coupon = couponRepository.findCouponById(command.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		UserCoupon userCoupon = UserCoupon.createIssuedCoupon(command.getUserId(), command.getCouponId());
		couponRepository.saveUserCoupon(userCoupon);

		coupon.issue();
		return coupon;
	}


	public Coupon useCoupon(CouponCommand.Use useCouponCommand) {
		// 사용할 수 있는 쿠폰인지 검사
		Coupon coupon = couponRepository.findCouponById(useCouponCommand.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		// 사용 상태로 변경
		UserCoupon userCoupon = couponRepository.findIssuedCouponByUserIdAndCouponId(useCouponCommand.getUserId(), useCouponCommand.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_HAVE_COUPON));

		userCoupon.use();

		// 쿠폰 정보 반환하여
		return coupon;
	}
}
