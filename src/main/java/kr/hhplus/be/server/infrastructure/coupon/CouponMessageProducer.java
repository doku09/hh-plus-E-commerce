package kr.hhplus.be.server.infrastructure.coupon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponMessageProducer {

	private final KafkaTemplate<String, String> kafkaTemplate;
	private static final String REQUEST_COUPON_TOPIC = "request-coupon";
	private final ObjectMapper objectMapper;

	public void send(CouponIssuedRequestMessage message) {
		try {
			log.info("쿠폰 발급 요청");

			String payload = objectMapper.writeValueAsString(message); // 직렬화
			kafkaTemplate.send(REQUEST_COUPON_TOPIC, String.valueOf(message.getCouponId()),payload);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
