package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentEventPublisher {

	private final ApplicationEventPublisher publisher;

	public void success(List<OrderCriteria.OrderItem> orderItems) {
	}
}
