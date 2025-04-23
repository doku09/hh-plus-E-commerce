package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

import java.time.LocalDateTime;

public class CouponRequest {

	@Getter
	public static class Create {
		private String name;
		private Long discountPrice;
		private CouponType couponType;
		private int quantity;
		private LocalDateTime useStartDate;
		private LocalDateTime expiredDate;

		public Create(String name, Long discountPrice, CouponType couponType, int quantity, LocalDateTime useStartDate, LocalDateTime expiredDate) {
			this.name = name;
			this.discountPrice = discountPrice;
			this.couponType = couponType;
			this.quantity = quantity;
			this.useStartDate = useStartDate;
			this.expiredDate = expiredDate;
		}


	}
}
