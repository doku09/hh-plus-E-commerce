package kr.hhplus.be.server.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ProductInfo {

	@Getter
	public static class Product {

		private long id;
		private String name;
		private int price;

		private Product(long id, String name, int price) {
			this.id = id;
			this.name = name;
			this.price = price;
		}

		public static Product of(long id, String name, int price) {
			return new Product(id, name, price);
		}
	}
}
