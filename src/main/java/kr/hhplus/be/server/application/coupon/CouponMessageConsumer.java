package kr.hhplus.be.server.application.coupon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCouponCommand;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CouponMessageConsumer {

	private static final String REQUEST_COUPON_TOPIC = "request-coupon";

	private final CouponService couponService;
	private final ObjectMapper objectMapper;

	@KafkaListener(topics = REQUEST_COUPON_TOPIC,  groupId = "request-coupon-consumer-group")
	public void handle(String message) {
		log.info("쿠폰 컨슈머 - 발급진행");
		try {
			CouponIssuedRequestMessage request = objectMapper.readValue(message, CouponIssuedRequestMessage.class);

			couponService.issueCouponWithLuaScript(UserCouponCommand.Issue.of(request.getCouponId(), request.getUserId()));
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
}
