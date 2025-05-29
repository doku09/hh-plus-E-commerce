package kr.hhplus.be.server.domain.bestItem;

import kr.hhplus.be.server.domain.order.OrderCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RankingUpdater {

	private final BestItemRepository bestItemRepository;

//	@Transactional(propagation = Propagation.REQUIRES_NEW) //커밋이후에 이벤트가 컨슘되는거라 데이터변경시 반영이 안된다.
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	public void handleOrderCompleted(OrderCompletedEvent event) {
		String LIVE_RANK_KEY = "ranking:live";

		log.info("실시간 인기상품 집계 리스너 실행");
		log.info("->OrderFacade --> RankingUpdater TransactionName:{}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("->OrderFacade --> RankingUpdater TransactionActive:{}", TransactionSynchronizationManager.isActualTransactionActive());

		for (OrderCompletedEvent.OrderItem orderItem : event.getOrderItems()) {
			bestItemRepository.incrSortedSet(LIVE_RANK_KEY, String.valueOf(orderItem.getProductId()), orderItem.getQuantity(), Duration.ofDays(1));
		}
	}
}
