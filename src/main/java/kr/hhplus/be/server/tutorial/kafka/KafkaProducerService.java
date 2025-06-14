package kr.hhplus.be.server.tutorial.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	private final KafkaTemplate<String,String> kafkaTemplate;

	public void sendmessage(String topic, String key, String message) {
		kafkaTemplate.send(topic, message);
	}
}
