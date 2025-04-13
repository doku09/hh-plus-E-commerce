package kr.hhplus.be.server.interfaces.product;

import lombok.Getter;

public class ProductCommand {

	@Getter
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
	}
}
