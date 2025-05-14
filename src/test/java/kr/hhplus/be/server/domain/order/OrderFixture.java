package kr.hhplus.be.server.domain.order;

import java.util.List;

public class OrderFixture {

	public static Order createOrderWithOrderItems(Long userId, OrderStatus status) {
		Order order = new Order(userId, status);

		order.addItems(
			List.of(
				OrderItem.of(1L, 1000L, 5),
				OrderItem.of(2L, 1000L, 4),
				OrderItem.of(3L, 1000L, 3)
			));

		return order;
	}

	public static Order createOrderWithOrderItems2(Long userId, OrderStatus status) {
		Order order = new Order(userId, status);

		order.addItems(
			List.of(
				OrderItem.of(1L, 1000L, 3),
				OrderItem.of(2L, 1000L, 2),
				OrderItem.of(3L, 1000L, 1)
			));

		return order;
	}

	public static Order createOrderWithOrderItems3(Long userId, OrderStatus status) {
		Order order = new Order(userId, status);

		order.addItems(
			List.of(
				OrderItem.of(1L, 1000L, 1),
				OrderItem.of(2L, 1000L, 2),
				OrderItem.of(3L, 1000L, 3)
			));

		return order;
	}

	public static Order createOrderWithOrderItems(Long productId,Long userId, OrderStatus status) {
		Order order = new Order(userId, status);

		order.addItem(OrderItem.of(productId, 1000L, 5));

		return order;
	}
}
