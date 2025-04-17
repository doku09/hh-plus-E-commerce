package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

public class CouponCommand {

	@Getter
	public static class Create {
		private final String name;
//		private final DiscountPolicy discountPolicy;
		private final Long discountPrice;
		private final int quantity;
		private final CouponType couponType;
		private final LocalDateTime useStartDate;
		private final LocalDateTime expiredDate;

		private Create(String name, Long discountPrice, int quantity, CouponType couponType, LocalDateTime useStartDate, LocalDateTime expiredDate) {
			this.name = name;
			this.quantity = quantity;
			this.couponType = couponType;
//			this.discountPolicy = discountPolicy;
			this.discountPrice = discountPrice;
			this.useStartDate = useStartDate;
			this.expiredDate = expiredDate;
		}

		public static Create of(String name, Long discountPrice, int quantity, CouponType couponType, LocalDateTime useStartDate, LocalDateTime expriedDate) {
			return new Create(name, discountPrice, quantity, couponType, useStartDate, expriedDate);
		}
	}

	@Getter
	public static class Use {
		private Long userId;
		private Long couponId;

		private Use(Long userId, Long couponId) {
			this.userId = userId;
			this.couponId = couponId;
		}

		public static Use of(Long userId, Long couponId) {
			return new Use(userId,couponId);
		}

	}
}
