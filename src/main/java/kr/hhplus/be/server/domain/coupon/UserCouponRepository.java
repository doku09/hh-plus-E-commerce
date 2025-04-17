package kr.hhplus.be.server.domain.coupon;

import java.util.List;
import java.util.Optional;

public interface UserCouponRepository {
	Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);

	List<UserCoupon> findByUserId(Long userId);

	void save(UserCoupon userCoupon);
}
