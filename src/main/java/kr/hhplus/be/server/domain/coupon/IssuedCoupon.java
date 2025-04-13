package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IssuedCoupon {

	private long id;
	private long userId;
	private long couponId;
	private LocalDateTime issuedAt;

	public IssuedCoupon(long userId, long couponId) {
		this.userId = userId;
		this.couponId = couponId;
		issuedAt = LocalDateTime.now();
	}

	public static IssuedCoupon createIssuedCoupon(long userId, long couponId) {
		return new IssuedCoupon(userId, couponId);
	}
}
