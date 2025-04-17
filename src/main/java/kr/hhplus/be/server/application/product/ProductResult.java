package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.ProductInfo;
import lombok.Getter;

import java.util.List;

public class ProductResult {

	@Getter
	public static class Product {

		private Long id;
		private String name;
		private Long price;

		private Product(Long id, String name, Long price) {
			this.id = id;
			this.name = name;
			this.price = price;
		}

		public static ProductResult.Product of(Long id, String name, Long price) {
			return new ProductResult.Product(id, name, price);
		}
	}

	public static class TopOrderedProducts {
		private List<Product> products;

		private TopOrderedProducts(List<Product> products) {
			this.products = products;
		}

		public static ProductResult.TopOrderedProducts of(List<Product> products) {
			return new ProductResult.TopOrderedProducts(products);
		}

	}
}
