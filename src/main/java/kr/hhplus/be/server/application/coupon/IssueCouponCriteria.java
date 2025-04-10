package kr.hhplus.be.server.application.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IssueCouponCriteria {

	private long couponId;
	private long userId;
	private LocalDateTime issuedAt;
}
