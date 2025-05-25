package kr.hhplus.be.server.application.notify;

import kr.hhplus.be.server.application.order.OrderCompletedEvent;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Component
@RequiredArgsConstructor
public class NotifyEventListener {

	private final UserRepository userRepository;

	@Transactional(propagation = Propagation.REQUIRES_NEW)
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	@Async
	public void notifyOrder(OrderCompletedEvent event) {
		log.info("주문 완료 알림 발송 리스너 실행");
		log.info("->OrderFacade --> NotifyEventListener TransactionName:{}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("->OrderFacade --> NotifyEventListener TransactionActive:{}", TransactionSynchronizationManager.isActualTransactionActive());

		userRepository.save(User.create("테스트"));
//		log.info("->OrderFacade --> NotifyEventListener isSynchronizationActive:{}", TransactionSynchronizationManager.isSynchronizationActive());

//		log.info("->OrderFacade --> NotifyEventListener Thread.currentThread().getName:{}",Thread.currentThread().getName());
//		log.info("->OrderFacade --> NotifyEventListener TransactionSynchronizationManager.getResourceMap():{}",TransactionSynchronizationManager.getResourceMap());
	}
}
