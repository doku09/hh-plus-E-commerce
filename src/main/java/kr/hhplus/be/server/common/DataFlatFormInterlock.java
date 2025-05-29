package kr.hhplus.be.server.common;

import kr.hhplus.be.server.infrastructure.order.OrderCompletedMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DataFlatFormInterlock {

	private static final String ORDER_COMPLETED_TOPIC = "order-completed";

	@KafkaListener(topics = ORDER_COMPLETED_TOPIC,  containerFactory = "orderCompletedKafkaListenerContainerFactory")
	public void consume(OrderCompletedMessage message) throws InterruptedException {
		log.info("consume message={}",message);
		Thread.sleep(3000);
	}
}
