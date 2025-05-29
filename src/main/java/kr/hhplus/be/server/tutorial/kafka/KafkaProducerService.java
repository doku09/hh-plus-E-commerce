package kr.hhplus.be.server.tutorial.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	private final KafkaTemplate<String,String> kafkaTemplate;

	public void sendmessage(String topic, String key, String message) {
		log.info("produce message = {}",message);
		kafkaTemplate.send(topic, message);
	}
}
