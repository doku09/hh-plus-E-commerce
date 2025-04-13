package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.ProductCommand.Create;
import lombok.Getter;

public class ProductCommand {

	@Getter
	public static class Create {
		private final String name;
		private final int price;

		private Create(String name, int price) {
			this.name = name;
			this.price = price;
		}

		public static Create of(String name, int price) {
			return new Create(name, price);
		}
	}
}
