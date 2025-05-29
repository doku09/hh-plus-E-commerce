package kr.hhplus.be.server.domain.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class OrderCompletedMessage {

	private LocalDateTime occurredAt = LocalDateTime.now();

	private Long orderId;

	public OrderCompletedMessage(Long orderId) {
		this.orderId = orderId;
	}
}
