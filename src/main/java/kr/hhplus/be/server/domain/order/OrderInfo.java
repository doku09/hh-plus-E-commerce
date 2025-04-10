package kr.hhplus.be.server.domain.order;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class OrderInfo {

	private long id;
	private int totalPrice;
	private OrderStatus status;
	private long userId;
	private LocalDateTime createdAt;

	public OrderInfo(long id, int totalPrice, OrderStatus status, long userId,LocalDateTime createdAt) {
		this.id = id;
		this.totalPrice = totalPrice;
		this.status = status;
		this.userId = userId;
		this.createdAt = createdAt;
	}

	public static OrderInfo from(Order order) {
		return new OrderInfo(order.getId(),order.getTotalPrice(),order.getStatus(),order.getUser().getId(), order.getCreatedAt());
	}
}
