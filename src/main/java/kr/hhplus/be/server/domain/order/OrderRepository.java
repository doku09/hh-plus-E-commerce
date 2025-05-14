package kr.hhplus.be.server.domain.order;

import java.util.List;

public interface OrderRepository {
	void save(Order order);

	List<OrderItem> findAllOrderItemsByIds(List<Long> orderIds);

	Order findById(Long orderId);

	List<OrderItem> getOrderBeforeHour(int hour);
	List<OrderItem> getOrderBeforeDay(int day);
}
