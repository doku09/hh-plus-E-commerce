package kr.hhplus.be.server.domain.coupon.event;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CouponIssuedRequestMessage {
	private Long userId;
	private Long couponId;

	public CouponIssuedRequestMessage(Long userId, Long couponId) {
		this.userId = userId;
		this.couponId = couponId;
	}
}
