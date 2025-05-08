package kr.hhplus.be.server.domain.order;

public class OrderFixture {

	public static Order createOrderWithOrderItems(Long userId, OrderStatus status) {
		Order order = new Order(userId, status);

		order.addItem(OrderItem.of(1L, 1000L, 5));
		order.addItem(OrderItem.of(2L, 1000L, 4));
		order.addItem(OrderItem.of(3L, 1000L, 3));

		return order;
	}

	public static Order createOrderWithOrderItems(Long productId,Long userId, OrderStatus status) {
		Order order = new Order(userId, status);

		order.addItem(OrderItem.of(productId, 1000L, 5));

		return order;
	}
}
