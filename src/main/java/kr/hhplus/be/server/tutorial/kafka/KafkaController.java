package kr.hhplus.be.server.tutorial.kafka;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/kafka")
public class KafkaController {

	private final KafkaProducerService kafkaProducerService;

	@PostMapping("/send")
	public ResponseEntity<String> send(@RequestParam String key, @RequestParam String message) {
		kafkaProducerService.sendmessage("test-topic", key, message);
		return ResponseEntity.ok("Message sent!");
	}
}
