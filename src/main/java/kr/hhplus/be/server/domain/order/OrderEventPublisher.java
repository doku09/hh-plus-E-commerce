package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.application.order.OrderCompletedEvent;
import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventPublisher {

	private final ApplicationEventPublisher publisher;

	public void success(List<OrderCriteria.OrderItem> orderItems) {
		publisher.publishEvent(new OrderCompletedEvent(orderItems));
	}
}
