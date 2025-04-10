package kr.hhplus.be.server.domain.coupon;

public interface QuantityPolicy {
	boolean canIssue(Integer quantity);
}
