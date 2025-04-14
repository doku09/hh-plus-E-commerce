package kr.hhplus.be.server.domain.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	public OrderInfo.Order order(Order order) {

		orderRepository.save(order);

		return OrderInfo.Order.of(order.getId(),order.getTotalPrice() ,order.getStatus());
	}
}
