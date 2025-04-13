package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CouponInfo {

	@Getter
	public static class Coupon {
		private String name;
		private LocalDateTime useStartDate;
		private LocalDateTime expiredDate;
		private int quantity;
		private CouponType couponType;

		public Coupon(String name, int quantity, CouponType couponType,LocalDateTime useStartDate, LocalDateTime expiredDate) {
			this.name = name;
			this.couponType = couponType;
			this.quantity = quantity;
			this.useStartDate = useStartDate;
			this.expiredDate = expiredDate;
		}

		public static Coupon of(String name, LocalDateTime useStartDate, LocalDateTime expiredDate, int quantity,CouponType couponType) {
			return new Coupon(name, quantity, couponType, useStartDate, expiredDate);
		}
	}



}
