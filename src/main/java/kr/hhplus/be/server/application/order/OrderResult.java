package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderStatus;

import java.time.LocalDateTime;

// TODO QUESTION) OrderService에서 반환하는 OrderInfo로 사용하면 안되나요?
public class OrderResult {

	private long id;
	private int totalPrice;
	private OrderStatus orderStatus;
	private LocalDateTime createdAt;

	public OrderResult(long id, int totalPrice, OrderStatus orderStatus, LocalDateTime createdAt) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.orderStatus = orderStatus;
		this.createdAt = createdAt;
	}

	public static OrderResult from(OrderInfo orderInfo) {
		return new OrderResult(orderInfo.getId(), orderInfo.getTotalPrice(),orderInfo.getStatus(),orderInfo.getCreatedAt());
	}
}
