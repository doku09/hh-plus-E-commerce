package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

	private final CouponJpaRepository couponJpaRepository;
	private final CouponRedisRepository redisRepository;
	private final UserCouponJpaRepository userCouponRepository;

	// 쿠폰등록
	@Override
	public Coupon saveCoupon(Coupon coupon) {
		return couponJpaRepository.save(coupon);
	}

	// 쿠폰조회
	@Override
	public Optional<Coupon> findCouponById(long id) {
		return couponJpaRepository.findById(id);
	}

	@Override
	public Optional<Coupon> findByIdUpdate(long id) {
		return couponJpaRepository.findByIdUpdate(id);
	}

	// 쿠폰 발행
	@Override
	public void saveUserCoupon(UserCoupon userCoupon) {
		userCouponRepository.save(userCoupon);
	}

	// 유저에게 발급된 쿠폰목록 조회
	@Override
	public List<UserCoupon> findUserCouponByUserId(long userId) {
		return userCouponRepository.findByUserId(userId);
	}

	// 특정유저에게 발행된 특정 쿠폰 조회
	@Override
	public Optional<UserCoupon> findIssuedCouponByUserIdAndCouponId(Long userId, Long couponId) {
		return userCouponRepository.findByUserIdAndCouponId(userId, couponId);
	}

	@Override
	public List<UserCoupon> findAllUserCoupon() {
		return userCouponRepository.findAll();
	}

	@Override
	public void decrString(String couponKey) {
		redisRepository.decrString(couponKey);
	}

	@Override
	public String getString(String couponKey) {
		return redisRepository.getString(couponKey);
	}

	@Override
	public void setString(String couponKey, String value) {
		redisRepository.setString(couponKey, value);
	}
	@Override
	public void expire(String couponKey, Duration duration) {
		redisRepository.expire(couponKey,duration);
	}

	@Override
	public Long addSet(String key, String value) {
		return redisRepository.addSet(key,value);
	}

	@Override
	public Long execute(DefaultRedisScript<Long> script, List<String> remKey, String s) {
		return redisRepository.execute(script,remKey,s);
	}
}
