package kr.hhplus.be.server.domain.coupon;

public class LimitedQuantity implements QuantityPolicy{

	private int maxQuantity;

	public LimitedQuantity(int maxQuantity) {
		this.maxQuantity = maxQuantity;
	}

	@Override
	public Integer getQuantity() {
		return maxQuantity;
	}

	@Override
	public boolean canIssue() {
		return maxQuantity > 0;
	}

	@Override
	public void substractCoupon() {
		maxQuantity--;
	}
}
