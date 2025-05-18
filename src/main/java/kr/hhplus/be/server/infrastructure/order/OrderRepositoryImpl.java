package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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
	public List<OrderItem> getOrderBeforeHour(int hours) {
		return getOrderItemsWithin(hours, ChronoUnit.HOURS);
	}

	@Override
	public List<OrderItem> getOrderBeforeDay(int days) {
		return getOrderItemsWithin(days, ChronoUnit.DAYS);
	}

	private List<OrderItem> getOrderItemsWithin(long amount, ChronoUnit unit) {
		LocalDateTime now = LocalDateTime.now();
		LocalDateTime start = now.minus(amount, unit);

		// 1) 기간 안에 생성된 Order 조회
		List<Order> orders = orderJpaRepository.getPaidOderWithDate(start, now);

		// 2) 해당 Order들의 아이템만 다시 조회
		List<Long> orderIds = orders.stream()
			.map(Order::getId)
			.toList();

		return orderJpaRepository.findAllOrderItemsByIds(orderIds);
	}


}
