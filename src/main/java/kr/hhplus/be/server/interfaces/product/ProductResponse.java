package kr.hhplus.be.server.interfaces.product;

public class ProductResponse {

	public static class Product {

		private Long id;
		private String name;
		private int price;

		private Product(Long id, String name, int price) {
			this.id = id;
			this.name = name;
			this.price = price;
		}

		public Product of(Long id, String name, int price) {
			return new Product(id,name,price);
		}
	}
}
