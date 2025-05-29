package kr.hhplus.be.server.tutorial.kafka;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class KafkaProducerServiceTest {

	@Autowired private KafkaProducerService kafkaProducerService;

	@Test
	@DisplayName("카프카 간단한 메시지를 주고 받는다.")
	void kafka_sendMessage() throws InterruptedException {

	  // given
		  kafkaProducerService.sendmessage("test-topic","userA","order1");
	  // when
		Thread.sleep(3000); // 3초 정도 기다려서 로그 확인 (테스트에서는 이렇게 강제 대기 필요)
	  // then

	}


}