package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.product.ProductCommand.Create;
import lombok.Getter;

import java.util.List;

public class ProductCommand {

	@Getter
	public static class Create {
		private final String name;
		private final Long price;
		private final int quantity;

		private Create(String name, Long price, int quantity) {
			this.name = name;
			this.price = price;
			this.quantity = quantity;
		}

		public static Create of(String name, Long price, int quantity) {
			return new Create(name, price, quantity);
		}
	}

	@Getter
	public static class OrderProducts {

		private final List<OrderProduct> orderProducts;

		public OrderProducts(List<OrderProduct> orderProducts) {
			this.orderProducts = orderProducts;
		}

		public static OrderProducts of(List<OrderProduct> orderProducts) {
			return new OrderProducts(orderProducts);
		}
	}

	@Getter
	public static class OrderProduct {
		private final Long productId;
		private final int quantity;

		private OrderProduct(Long productId, int quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}

		public static OrderProduct of(Long productId, int quantity) {
			return new OrderProduct(productId, quantity);
		}
	}
}
