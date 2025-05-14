package kr.hhplus.be.server.application.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCompletedEvent {
	private List<OrderCriteria.OrderItem> items;

	public OrderCompletedEvent(List<OrderCriteria.OrderItem> items) {
		this.items = items;
	}
}
