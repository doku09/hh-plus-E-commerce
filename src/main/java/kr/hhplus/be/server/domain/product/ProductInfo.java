package kr.hhplus.be.server.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ProductInfo {

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

		public static Product of(Long id, String name, Long price) {
			return new Product(id, name, price);
		}
	}

	@Getter
	public static class OrderProducts {

		private final List<OrderProduct> orderProducts;

		private OrderProducts(List<OrderProduct> orderProducts) {
			this.orderProducts = orderProducts;
		}

		public static OrderProducts of(List<OrderProduct> orderProducts) {
			return new OrderProducts(orderProducts);
		}
	}

	@Getter
	public static class OrderProduct {

		private final Long productId;
		private final String productName;
		private final Long productPrice;
		private final int quantity;

		@Builder
		private OrderProduct(Long productId, String productName, Long productPrice, int quantity) {
			this.productId = productId;
			this.productName = productName;
			this.productPrice = productPrice;
			this.quantity = quantity;
		}

		public static OrderProduct of(Long productId, String productName, Long productPrice, int quantity) {
			return new OrderProduct(productId, productName, productPrice, quantity);
		}
	}


	@Getter
	public static class TopProducts {

		private List<Product> products;

		private TopProducts(List<Product> products) {
			this.products = products;
		}

		public static TopProducts of(List<Product> products) {
			return new TopProducts(products);
		}
	}
}
