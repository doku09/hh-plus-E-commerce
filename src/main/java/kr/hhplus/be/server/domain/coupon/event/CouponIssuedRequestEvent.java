package kr.hhplus.be.server.domain.coupon.event;

import lombok.Getter;

@Getter
public class CouponIssuedRequestEvent {
	private Long userId;
	private Long couponId;

	public CouponIssuedRequestEvent(Long userId, Long couponId) {
		this.userId = userId;
		this.couponId = couponId;
	}
}
