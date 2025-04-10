package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.BaseTimeEntity;
import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

//TODO QUESTION Getter 지양인데 OrderResponse를 바인딩해줘야되는데 만들어야 할까요?
@Getter
public class Order extends BaseTimeEntity {

	private long id;
	private int totalPrice;
	private OrderStatus status;
	private User user;

	private Order(int totalPrice, User user, OrderStatus orderStatus, LocalDateTime createdAt) {
		this.totalPrice = totalPrice;
		this.user = user;
		this.status = orderStatus;
		super.createdAt = createdAt;
	}

	public static Order createOrder(User user, List<OrderItem> orderItems) {

		// orderItems를 돌면서 주문의 총합을 계산한다.
		int totalPrice = orderItems.stream()
			.mapToInt(OrderItem::getTotalPrice)
			.sum();

		// orderItem에 order를 바인딩해주기 위해 생성한다.
		Order order = new Order(totalPrice, user, OrderStatus.ORDERED,LocalDateTime.now());

		// 루프를 돌면서 order를 할당
		for (OrderItem item : orderItems) {
			item.assignOrder(order);
		}

		return order;
	}
}
