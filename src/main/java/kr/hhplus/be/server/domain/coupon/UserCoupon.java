package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserCoupon {

	private long id;
	private long userId;
	private long couponId;
	private IssuedCouponStatus status;
	private LocalDateTime issuedAt;
	private LocalDateTime usedAt;

	public UserCoupon(long userId, long couponId) {
		this.userId = userId;
		this.couponId = couponId;
		issuedAt = LocalDateTime.now();
	}

	public static UserCoupon createIssuedCoupon(long userId, long couponId) {
		return new UserCoupon(userId, couponId);
	}

	public boolean isSameCoupon(Long couponId) {
		return this.couponId == couponId;
	}

	public void used() {
		this.status = IssuedCouponStatus.USED;
		usedAt = LocalDateTime.now();
	}

	public boolean isUsed() {
		return this.status == IssuedCouponStatus.USED;
	}
}
