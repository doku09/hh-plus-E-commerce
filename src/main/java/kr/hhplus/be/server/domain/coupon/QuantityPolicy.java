package kr.hhplus.be.server.domain.coupon;

public interface QuantityPolicy {
	Integer getQuantity();
	boolean canIssue();
	void substractCoupon();
}
