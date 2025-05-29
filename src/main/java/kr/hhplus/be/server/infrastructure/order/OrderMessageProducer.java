package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.OrderCompletedMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderMessageProducer {

	private final KafkaTemplate<String, OrderCompletedMessage> kafkaTemplate;
	private static final String ORDER_COMPLETED_TOPIC = "order-completed";

	public void send(OrderCompletedMessage message) {
		kafkaTemplate.send(ORDER_COMPLETED_TOPIC, String.valueOf(message.getOrderId()),message);
	}
}
