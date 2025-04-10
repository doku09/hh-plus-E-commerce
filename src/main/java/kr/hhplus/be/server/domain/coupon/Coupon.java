package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Coupon {

	private long id;
	private String name;
	private Integer quantity;
	private DiscountPolicy discountPolicy;
	private QuantityPolicy quantityPolicy;
	private LocalDateTime useStartDate;
	private LocalDateTime expiredDate;

	public boolean canIssue() {
		return quantityPolicy.canIssue(quantity);
	}

	public void issue() {
		if(!canIssue()) {
			throw new GlobalBusinessException(ErrorCode.NOT_ENOUGH_COUPON);
		}

		quantity--;
	}

	// 수량제한이 있는 쿠폰 생성
	public Coupon(String name, DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate,int quantity) {
		this.name = name;
		this.discountPolicy = discountPolicy;
		this.useStartDate = useStartDate;
		this.expiredDate = expiredDate;
		this.quantity = quantity;
		this.quantityPolicy = new LimitedQuantity(quantity);
	}

	// 수량제한이 없는 쿠폰 생성
	public Coupon(String name, DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate) {
		this.name = name;
		this.discountPolicy = discountPolicy;
		this.useStartDate = useStartDate;
		this.expiredDate = expiredDate;
		this.quantityPolicy = new UnlimitedQuantity();
	}


	public static Coupon createUnLimitedCoupon(String name, DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate) {

		return new Coupon(name, discountPolicy, useStartDate, expiredDate);
	}

	public static Coupon createLimitedCoupon(String name, DiscountPolicy discountPolicy, LocalDateTime useStartDate, LocalDateTime expiredDate,int quantity) {
		return new Coupon(name, discountPolicy, useStartDate, expiredDate,quantity);
	}


}
