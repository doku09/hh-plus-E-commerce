package kr.hhplus.be.server.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderInfo order(OrderCreateCommand command) {

		Order order = Order.createOrder(command.getUser(),command.getOrderItems());

		orderRepository.save(order);

		return OrderInfo.from(order);
	}

	//TODO 주문 환불, 취소
}
