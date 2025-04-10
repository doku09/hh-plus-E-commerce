package kr.hhplus.be.server.domain.coupon;

public class UnlimitedQuantity implements QuantityPolicy{
	@Override
	public boolean canIssue(Integer issuedCount) {
		return true;
	}
}
