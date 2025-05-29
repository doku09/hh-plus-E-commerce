package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.infrastructure.order.OrderCompletedMessage;
import kr.hhplus.be.server.infrastructure.order.OrderMessageProducer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderEventListener {

	private final OrderMessageProducer producer;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	public void handle(OrderCompletedEvent event) {
		producer.send(new OrderCompletedMessage(event.getOrderId()));
	}
}
