package kr.hhplus.be.server.domain.coupon;

public enum DiscountPolicy {
	FIXED_AMOUNT {
		@Override
		public int caculateDiscount(int price, int value) {
			return value;
		}
	},PERCENTAGE {
		@Override
		public int caculateDiscount(int price, int value) {
			return price * value / 100;
		}
	};

	public abstract int caculateDiscount(int price, int value);
}
