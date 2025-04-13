package kr.hhplus.be.server.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderInfo.Order order(OrderCommand.Create command) {

		List<OrderItem> orderItems = command.getOrderItems().stream().map((o) -> OrderItem.of(o.getProductId(), o.getProductPrice(), o.getQuantity())).toList();

		Order order = Order.createOrder(command.getUserId(), orderItems);

		orderRepository.save(order);

		return OrderInfo.Order.of(order.getId(),order.getTotalPrice() ,order.getStatus());
	}

	//TODO 주문 환불, 취소
}
