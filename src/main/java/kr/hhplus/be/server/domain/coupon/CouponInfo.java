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
		private Long discountPrice;
		private int quantity;
		private CouponType couponType;

		public Coupon(Long id, String name, LocalDateTime useStartDate, LocalDateTime expiredDate, Long discountPrice, int quantity, CouponType couponType) {
			this.id = id;
			this.name = name;
			this.useStartDate = useStartDate;
			this.expiredDate = expiredDate;
			this.discountPrice = discountPrice;
			this.quantity = quantity;
			this.couponType = couponType;
		}

		private Coupon(Long id, String name, int quantity, Long discountPrice) {
			this.id = id;
			this.name = name;
			this.quantity = quantity;
			this.discountPrice = discountPrice;
		}

		public static Coupon of(Long id, String name, Long discountPrice, LocalDateTime useStartDate, LocalDateTime expiredDate, int quantity, CouponType couponType) {
			return new Coupon(id, name, useStartDate, expiredDate, discountPrice, quantity, couponType);
		}

		public static Coupon info(Long id, String name, int quantity, Long discountPrice) {
			return new Coupon(id,name,quantity, discountPrice);
		}

	}



}
