package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.order.OrderCompletedEvent;

public interface OrderEventPublisher {

	void publish(OrderCompletedEvent event);
}
