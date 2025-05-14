package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
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

	@Override
	public Order findById(Long orderId) {
		return orderJpaRepository.findById(orderId).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_ORDER));
	}

	@Override
	public List<OrderItem> getOrderBeforeHour(int hour) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime beforeHours = now.minusHours(hour);
		List<Order> orders = orderJpaRepository.getOrderBefore(beforeHours,now);

		List<Long> orderIds = orders.stream().map(Order::getId).toList();
		return orderJpaRepository.findAllOrderItemsByIds(orderIds);
	}

	@Override
	public List<OrderItem> getOrderBeforeDay(int day) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime beforeDays = now.minusDays(day);
		List<Order> orders = orderJpaRepository.getOrderBefore(beforeDays,now);

		List<Long> orderIds = orders.stream().map(Order::getId).toList();
		return orderJpaRepository.findAllOrderItemsByIds(orderIds);
	}


}
