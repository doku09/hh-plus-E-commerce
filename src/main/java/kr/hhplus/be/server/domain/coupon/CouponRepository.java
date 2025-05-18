package kr.hhplus.be.server.domain.coupon;

import org.springframework.data.redis.core.script.DefaultRedisScript;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

public interface CouponRepository {
	Coupon saveCoupon(Coupon entity);

	Optional<Coupon> findCouponById(long id);

	Optional<Coupon> findByIdUpdate(long id);

	void saveUserCoupon(UserCoupon userCoupon);

	List<UserCoupon> findUserCouponByUserId(long userId);

	Optional<UserCoupon> findIssuedCouponByUserIdAndCouponId(Long userId, Long couponId);

	List<UserCoupon> findAllUserCoupon();

	void decrString(String couponKey);

	String getString(String couponKey);

	void setString(String couponKey, String s);

	void expire(String couponKey, Duration duration);

	Long addSet(String issuedKey, String s);

	Long execute(DefaultRedisScript<Long> script, List<String> remKey, String s);
}
