package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;

	// 쿠폰 등록
	public CouponInfo.Coupon register(CouponCommand.Create command) {
		Coupon coupon = Coupon.create(command.getName(), command.getDiscountPrice(), command.getQuantity(),command.getCouponType() ,command.getUseStartDate(), command.getExpiredDate());

		Coupon savedCoupon = couponRepository.saveCoupon(coupon);

		return CouponInfo.Coupon.of(
				savedCoupon.getId(),
				savedCoupon.getName(),
				savedCoupon.getDiscountPrice(),
				savedCoupon.getUseStartDate(),
				savedCoupon.getExpiredDate(),
				savedCoupon.getCouponType()
		);
	}

	public Coupon findCouponById(long couponId) {
		return couponRepository.findCouponById(couponId).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));
	}


	@Transactional
	public CouponInfo.Coupon issueCoupon(UserCouponCommand.Issue command) {

		// 비관적락 조회
		Coupon findCoupon = couponRepository.findByIdUpdate(command.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		// 락 없는 조회
//		Coupon findCoupon = couponRepository.findCouponById(command.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		List<UserCoupon> userCouponIds = couponRepository.findUserCouponByUserId(command.getUserId());

		// 첫번째 요청 통과
		// 쿠폰을 이미 가지고있는지 검사
		boolean hasCoupon = userCouponIds.stream().anyMatch(userCoupon -> userCoupon.isSameCoupon(command.getCouponId()));

		if (hasCoupon) {
			throw new GlobalBusinessException(ErrorCode.ALREADY_ISSUED_COUPON);
		}

		//첫번째 요청만 업데이트
		findCoupon.issue();

		UserCoupon userCoupon = UserCoupon.createIssuedCoupon(command.getUserId(), command.getCouponId());

		couponRepository.saveUserCoupon(userCoupon);

		return CouponInfo.Coupon.info(findCoupon.getId(), findCoupon.getName(), findCoupon.getQuantity(), findCoupon.getDiscountPrice());
	}

	public CouponInfo.Coupon useCoupon(CouponCommand.Use useCouponCommand) {
		// 사용할 수 있는 쿠폰인지 검사
		Coupon coupon = couponRepository.findCouponById(useCouponCommand.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		// 사용 상태로 변경
		UserCoupon userCoupon = couponRepository.findIssuedCouponByUserIdAndCouponId(useCouponCommand.getUserId(), useCouponCommand.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_HAVE_COUPON));

		userCoupon.use();

		// 쿠폰 정보 반환하여
		return CouponInfo.Coupon.of(
			coupon.getId(),
			coupon.getName(),
			coupon.getDiscountPrice(),
			coupon.getUseStartDate(),
			coupon.getExpiredDate(),
			coupon.getCouponType()
		);
	}
}
