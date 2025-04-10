package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponInfo {

	private long id;
	private String name;
	private DiscountPolicy discountPolicy;
	private LocalDateTime useStartDate;
	private LocalDateTime expiredDate;
	private Integer quantity;

	public CouponInfo(long id, String name, DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate,Integer quantity) {
		this.id = id;
		this.name = name;
		this.discountPolicy = discountPolicy;
		this.useStartDate = useStartDate;
		this.expiredDate = expiredDate;
		this.quantity = quantity;
	}

	public static CouponInfo of(long id, String name, DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate,Integer quantity) {
		return new CouponInfo(id, name, discountPolicy, useStartDate, expiredDate, quantity);
	}
}
