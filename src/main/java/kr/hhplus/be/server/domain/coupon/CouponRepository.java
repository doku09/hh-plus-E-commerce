package kr.hhplus.be.server.domain.coupon;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponRepository {
	Coupon saveCoupon(Coupon entity);

	Optional<Coupon> findCouponById(long id);

	void issueCoupon(UserCoupon userCoupon);

	List<UserCoupon> findIssuedCouponByUserId(long id);

	UserCoupon findIssuedCouponByUserIdAndCouponId(Long userId, Long couponId);
}
