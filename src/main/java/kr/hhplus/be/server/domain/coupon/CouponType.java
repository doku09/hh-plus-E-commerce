package kr.hhplus.be.server.domain.coupon;

public enum CouponType {
	INFINITE("무제한"),
	LIMITED("제한");

	private final String description;

	CouponType(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

}
