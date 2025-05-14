package kr.hhplus.be.server.common;

import kr.hhplus.be.server.application.order.OrderCompletedEvent;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.infrastructure.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.*;

@Component
@RequiredArgsConstructor
public class RankingUpdater {

	private final RedisRepository redisRepository;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void handleOrderCompleted(OrderCompletedEvent event) {
		String LIVE_RANK_KEY = "ranking:live";

		for (OrderCriteria.OrderItem item : event.getItems()) {
			redisRepository.incrSortedSet(LIVE_RANK_KEY, String.valueOf(item.getProductId()), item.getQuantity(), Duration.ofDays(1));
		}
	}
}
