package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;

import java.time.LocalDateTime;

public class IssuedCoupon {

	private long id;
	private User user;
	private Coupon coupon;
	private LocalDateTime issuedAt;

	public IssuedCoupon(User user, Coupon coupon) {
		this.user = user;
		this.coupon = coupon;
		this.issuedAt = LocalDateTime.now();
	}

	public static IssuedCoupon createIssuedCoupon(User user, Coupon coupon) {
		return new IssuedCoupon(user,coupon);
	}
}
