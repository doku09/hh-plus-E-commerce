package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public void save(Order order) {
		orderJpaRepository.save(order);
	}

	@Override
	public List<OrderItem> findAllOrderItemsByIds(List<Long> orderIds) {
		return orderJpaRepository.findAllOrderItemsByIds(orderIds);
	}
}
