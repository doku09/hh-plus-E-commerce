package kr.hhplus.be.server.domain.payment;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository {
	void save(Payment payment);

	List<Payment> getPaymentHistoryWithPaid(LocalDateTime startDate, LocalDateTime endDate, PayStatus payStatus);

	List<Payment> findByStatusAndCreatedAtBetween(PayStatus status,
	                                              LocalDateTime start,
	                                              LocalDateTime end);
}
