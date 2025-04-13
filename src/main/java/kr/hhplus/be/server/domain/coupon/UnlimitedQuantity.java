package kr.hhplus.be.server.domain.coupon;

public class UnlimitedQuantity implements QuantityPolicy{
	@Override
	public Integer getQuantity() {
		return null;
	}

	@Override
	public boolean canIssue() {
		return true;
	}

	@Override
	public void substractCoupon() {
	}
}
