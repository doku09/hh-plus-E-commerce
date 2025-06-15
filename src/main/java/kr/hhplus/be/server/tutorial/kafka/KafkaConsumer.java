package kr.hhplus.be.server.tutorial.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

	@KafkaListener(topics = "test-topic", groupId = "my-consumer-group")
	public void consume(String message) {
		log.info("Consumed message: {}",message);
	}
}
