package kr.hhplus.be.server.domain.product;

import lombok.Getter;

public class ProductStockCommand {

	@Getter
	public static class Deduct {
		private long productId;
		private int quantity;

		public Deduct(long productId, int quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}

		public static Deduct of(long productId, int quantity) {
			return new Deduct(productId, quantity);
		}
	}
}
