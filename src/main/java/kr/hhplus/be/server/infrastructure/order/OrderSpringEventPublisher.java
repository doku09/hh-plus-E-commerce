package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderCompletedEvent;
import kr.hhplus.be.server.domain.order.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class OrderSpringEventPublisher implements OrderEventPublisher {

	private final ApplicationEventPublisher publisher;

	@Override
	public void publish(OrderCompletedEvent event) {
		publisher.publishEvent(event);
	}
}
