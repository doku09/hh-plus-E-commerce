package kr.hhplus.be.server.domain.coupon;

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

}
