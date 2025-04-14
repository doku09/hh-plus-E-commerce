package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponInfo {

	@Getter
	public static class Coupon {
		private Long id;
		private String name;
		private LocalDateTime useStartDate;
		private LocalDateTime expiredDate;
//		private DiscountPolicy policy;
		private Long discountPrice;
		private int quantity;
		private CouponType couponType;

		private Coupon(Long id,String name, Long discountPrice, CouponType couponType,LocalDateTime useStartDate, LocalDateTime expiredDate) {
			this.name = name;
			this.couponType = couponType;
			this.quantity = quantity;
			this.useStartDate = useStartDate;
			this.expiredDate = expiredDate;
		}

		public static Coupon of(Long id,String name,Long discountPrice, LocalDateTime useStartDate, LocalDateTime expiredDate,CouponType couponType) {
			return new Coupon(id,name,discountPrice, couponType, useStartDate, expiredDate);
		}
	}



}
