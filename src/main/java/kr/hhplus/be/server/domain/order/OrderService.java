package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.DataFlatFormInterlock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;
	private final DataFlatFormInterlock dataFlatFormInterlock;

	public OrderInfo.Order createOrder(OrderCommand.Create command) {
		Order order = Order.createOrder(command.getUserId());
		List<OrderCommand.OrderItem> orderItems = command.getOrderItems();
		for (OrderCommand.OrderItem orderItem : orderItems) {
			order.addItem(OrderItem.of(orderItem.getProductId(), orderItem.getProductPrice(), orderItem.getQuantity()));
		}

		order.applyCoupon(command.getCouponId(), command.getDiscountPrice());

		orderRepository.save(order);

		return OrderInfo.Order.of(order.getId(), order.getTotalPrice(), order.getDiscountPrice(), order.getStatus());
	}

	public OrderInfo.TopOrder getTopOrder(OrderCommand.TopOrderedProducts topOrderedProducts) {
		List<OrderItem> findItems = orderRepository.findAllOrderItemsByIds(topOrderedProducts.getOrderIds());

		// 상품 아이디 별 주문아이템 그룹화
		Map<Long, List<OrderItem>> groups = findItems.stream()
			.collect(groupingBy(OrderItem::getProductId));

		// 그룹화된 주문아이템을 상품별 주문량순으로 내림차순 정렬 후, Top N개를 가져온다.
		List<PopularOrder> popularOrders = groups.entrySet().stream()
			.map(entry -> {
				Long itemId = entry.getKey();
				List<OrderItem> orderItems = entry.getValue();
				int totalQuantity = orderItems.stream().mapToInt(OrderItem::getQuantity).sum();
				return new PopularOrder(itemId, totalQuantity);
			})
			.sorted(Comparator.comparing(PopularOrder::getOrderQuantity).reversed())
			.limit(topOrderedProducts.getLimit())
			.toList();

		return OrderInfo.TopOrder.of(popularOrders);
	}

	public void updateStatusToPaid(Long orderId) {
		Order order = orderRepository.findById(orderId);
		order.changeStatus(OrderStatus.PAID);

		//TODO: 구현 필요
		dataFlatFormInterlock.sendToOrderInfo();
	}
}
