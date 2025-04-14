package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class Coupon {

	private final String name;
//	private final DiscountPolicy discountPolicy;
	private final Long discountPrice;
	private final CouponType couponType;
	private final LocalDateTime useStartDate;
	private final LocalDateTime expiredDate;
	private long id;
	private int quantity;

	public Coupon(long id, String name, Long discountPrice, Integer quantity, CouponType couponType, LocalDateTime useStartDate, LocalDateTime expiredDate) {
		this.id = id;
		this.name = name;
		this.couponType = couponType;
		this.quantity = quantity;
		this.discountPrice = discountPrice;
		this.useStartDate = useStartDate;
		this.expiredDate = expiredDate;
	}

	public Coupon(String name, Long discountPrice, Integer quantity, CouponType couponType, LocalDateTime useStartDate, LocalDateTime expiredDate) {
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

	public static Coupon of(Long id, String name, Long discountPrice, Integer quantity, CouponType coupontType, LocalDateTime useStartDate, LocalDateTime expiredDate) {
		return new Coupon(id, name, discountPrice, quantity, coupontType, useStartDate, expiredDate);
	}

	public boolean canIssue() {
		return couponType == CouponType.INFINITE || quantity > 0;
	}

	public void issue() {
		if (!canIssue()) {
			throw new GlobalBusinessException(ErrorCode.NOT_ENOUGH_COUPON);
		}

		quantity--;
	}
}
