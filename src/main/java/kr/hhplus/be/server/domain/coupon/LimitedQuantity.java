package kr.hhplus.be.server.domain.coupon;

public class LimitedQuantity implements QuantityPolicy{

	private final int maxQuantity;

	public LimitedQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	@Override
	public boolean canIssue(Integer quantity) {
		return quantity > 0;
	}
}
