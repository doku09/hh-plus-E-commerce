package kr.hhplus.be.server.domain.coupon;

import java.time.LocalDateTime;

public class CouponFixture {

	public static Coupon create(int quantity) {
		return Coupon.create("쿠폰",1000L,quantity,CouponType.LIMITED, LocalDateTime.now(),LocalDateTime.now().plusDays(10));
	}
}
