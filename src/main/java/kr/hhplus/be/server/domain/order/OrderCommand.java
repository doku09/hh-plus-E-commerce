package kr.hhplus.be.server.domain.order;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class OrderCommand {

	@Getter
	public static class Create {
		private final long userId;
		private OrderStatus orderStatus;
		private List<OrderItem> orderItems;

		private Create(long userId, List<OrderItem> orderItems) {
			this.orderStatus = OrderStatus.ORDERED;
			this.userId = userId;
			this.orderItems = orderItems;
		}

		private Create(long userId) {
			this.userId = userId;
			this.orderStatus = OrderStatus.ORDERED;
		}

		public static Create of(long userId, List<OrderItem> orderItems) {
			return new Create(userId, orderItems);
		}

		public static Create of(long userId) {
			return new Create(userId, new ArrayList<>());
		}

		public void addItem(OrderItem orderItem) {
			this.orderItems.add(orderItem);
		}

		public void changeStatus(OrderStatus orderStatus) {
			this.orderStatus = orderStatus;
		}
	}

	@Getter
	public static class OrderItem {
		private final long productId;
		private final int productPrice;
		private final int quantity;

		private OrderItem(long productId, int productPrice, int quantity) {
			this.productPrice = productPrice;
			this.productId = productId;
			this.quantity = quantity;
		}

		public static OrderItem of(long productId, int productPrice, int quantity) {
			return new OrderItem(productId, productPrice, quantity);
		}
	}
}
