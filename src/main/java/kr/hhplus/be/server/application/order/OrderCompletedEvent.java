package kr.hhplus.be.server.application.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCompletedEvent {

	private Long orderId;
	private List<OrderItem> orderItems;

	public OrderCompletedEvent(Long orderId) {
		this.orderId = orderId;
	}

	@Getter
	public static class OrderItem {
		private Long productId;
		private Long quantity;
	}
}
