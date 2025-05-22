package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.event.DomainEvent;
import lombok.Getter;

import java.util.List;

public class OrderEvent {

	@Getter
	public static class Created implements DomainEvent {
			private Long orderId;
			private Long userId;
			private List<OrderItemDto> items;

		public Created() {
		}

		public Created(Long orderId, Long userId, List<OrderItemDto> items) {
			this.orderId = orderId;
			this.userId = userId;
			this.items = items;
		}
	}
}
