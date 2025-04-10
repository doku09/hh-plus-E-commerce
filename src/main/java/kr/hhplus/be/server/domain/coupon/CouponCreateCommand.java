package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponCreateCommand {

	private String name;
	private DiscountPolicy discountPolicy;
	private LocalDateTime useStartDate;
	private LocalDateTime expiredDate;
	private Integer quantity;

	private CouponCreateCommand(String name, DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate, Integer quantity) {
		this.name = name;
		this.discountPolicy = discountPolicy;
		this.useStartDate = useStartDate;
		this.expiredDate = expiredDate;
		this.quantity = quantity;
	}

	// 수량 입력값이 있으면 수량이 있는 쿠폰도메인객체 생성
	public Coupon toEntity() {
		if (quantity == null) {
			return Coupon.createUnLimitedCoupon(name, discountPolicy, useStartDate, expiredDate);
		}
		return Coupon.createLimitedCoupon(name, discountPolicy, useStartDate, expiredDate, quantity);
	}

	public static CouponCreateCommand of(String name,DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate,Integer quantity) {
		return new CouponCreateCommand(name,discountPolicy,useStartDate,expiredDate,quantity);
	}
}
