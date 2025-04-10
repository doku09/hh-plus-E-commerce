package kr.hhplus.be.server.domain.coupon;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CouponRepository {
	Coupon saveCoupon(Coupon entity);

	Optional<Coupon> findCouponById(long id);

	void issueCoupon(IssuedCoupon issuedCoupon);
}
