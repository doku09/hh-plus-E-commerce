package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.domain.product.ProductCommand;

public class ProductRequest {

	public static class Create {
		private String name;
		private int price;

		private Create(String name, int price) {
			this.name = name;
			this.price = price;
		}

		public static Create of(String name, int price) {
			return new Create(name, price);
		}

		public ProductCommand.Create toCommand() {
			return ProductCommand.Create.of(name,price);
		}
	}
}
