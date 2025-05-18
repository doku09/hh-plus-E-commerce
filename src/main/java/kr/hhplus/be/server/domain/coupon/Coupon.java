package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
@DynamicUpdate
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String name;
	private Long discountPrice;
	private CouponType couponType;
	private LocalDateTime useStartDate;
	private LocalDateTime expiredDate;
	private int quantity;

	private Coupon(String name, Long discountPrice, Integer quantity, CouponType couponType, LocalDateTime useStartDate, LocalDateTime expiredDate) {

		 this.name = name;
		 this.discountPrice = discountPrice;
		 this.couponType = couponType;
		 this.quantity = quantity;
		 this.useStartDate = useStartDate;
		 this.expiredDate = expiredDate;
	}

	public static Coupon create(String name, Long discountPrice, Integer quantity, CouponType coupontType, LocalDateTime useStartDate, LocalDateTime expiredDate) {

		return new Coupon(name, discountPrice, quantity, coupontType, useStartDate, expiredDate);
	}


	public boolean canIssue() {
		return couponType == CouponType.INFINITE || quantity > 0;
	}

	public void issue() {
		if (!canIssue()) {
			throw new GlobalBusinessException(ErrorCode.NOT_ENOUGH_COUPON);
		}

		if (couponType == CouponType.LIMITED) {
			if (quantity <= 0) {
				throw new GlobalBusinessException(ErrorCode.NOT_ENOUGH_COUPON);
			}

			quantity--;
		}

	}
}
