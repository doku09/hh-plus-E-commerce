package kr.hhplus.be.server.domain.bestItem;

import kr.hhplus.be.server.application.order.OrderCompletedEvent;
import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.*;

@Component
@RequiredArgsConstructor
public class RankingUpdater {

	private final BestItemRepository bestItemRepository;

	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	public void handleOrderCompleted(OrderCompletedEvent event) {
		String LIVE_RANK_KEY = "ranking:live";

		for (OrderCriteria.OrderItem item : event.getItems()) {
			bestItemRepository.incrSortedSet(LIVE_RANK_KEY, String.valueOf(item.getProductId()), item.getQuantity(), Duration.ofDays(1));
		}
	}
}
