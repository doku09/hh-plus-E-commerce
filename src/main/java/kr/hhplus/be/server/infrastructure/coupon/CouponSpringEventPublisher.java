package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.event.CouponEventPublisher;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedRequestEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponSpringEventPublisher implements CouponEventPublisher {

	private final ApplicationEventPublisher publisher;

	@Override
	public void publish(CouponIssuedRequestEvent event) {
		publisher.publishEvent(event);
	}
}
