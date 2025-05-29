package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

@Getter
public class RequestIssuedCouponCommand {

	private Long userId;
	private Long couponId;

	public RequestIssuedCouponCommand(Long userId, Long couponId) {
		this.userId = userId;
		this.couponId = couponId;
	}
}
