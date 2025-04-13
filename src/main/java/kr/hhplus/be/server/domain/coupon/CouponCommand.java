package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

public class CouponCommand {

	@Getter
	public static class Create {
		private final String name;
		private final DiscountPolicy discountPolicy;
		private final int quantity;
		private final CouponType couponType;
		private final LocalDateTime useStartDate;
		private final LocalDateTime expiredDate;

		private Create(String name, DiscountPolicy discountPolicy, int quantity, CouponType couponType, LocalDateTime useStartDate, LocalDateTime expiredDate) {
			this.name = name;
			this.quantity = quantity;
			this.couponType = couponType;
			this.discountPolicy = discountPolicy;
			this.useStartDate = useStartDate;
			this.expiredDate = expiredDate;
		}

		public static Create of(String name, DiscountPolicy discountPolicy, int quantity, CouponType couponType, LocalDateTime useStartDate, LocalDateTime expriedDate) {
			return new Create(name, discountPolicy, quantity, couponType, useStartDate, expriedDate);
		}
	}

}
