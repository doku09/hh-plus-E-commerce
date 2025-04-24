package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class OrderRequest {

	@Getter
	@NoArgsConstructor
	public static class CreateOrder {
		private Long userId;
		List<OrderItem> orderItems;
		private Long couponId;

		private CreateOrder(Long userId, List<OrderItem> orderItems) {
			this.userId = userId;
			this.orderItems = orderItems;
		}

		public CreateOrder of(Long userId, List<OrderItem> orderItems) {
			return new CreateOrder(userId, orderItems);
		}

		public OrderCriteria.CreateOrder toCriteria() {
			return new OrderCriteria.CreateOrder(userId,couponId, orderItems.stream().map(OrderItem::toCriteria).toList());
		}
	}

	@Getter
	public static class OrderItem {
		private Long productId;
		private int quantity;

		private OrderItem(Long productId, int quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}

		public static OrderItem of(Long productId, int quantity) {
			return new OrderItem(productId, quantity);
		}

		public OrderCriteria.OrderItem toCriteria() {
			return OrderCriteria.OrderItem.of(productId, quantity);
		}
	}
}
