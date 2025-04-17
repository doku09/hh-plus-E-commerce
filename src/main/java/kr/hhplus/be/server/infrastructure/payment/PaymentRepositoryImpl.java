package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.PayStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PaymentRepositoryImpl implements PaymentRepository {

	private final PaymentJpaRepository paymentJpaRepository;

	@Override
	public void save(Payment payment) {
		paymentJpaRepository.save(payment);
	}

	@Override
	public List<Payment> getPaymentHistoryWithPaid(LocalDateTime startDate, LocalDateTime endDate, PayStatus payStatus) {
		return List.of();
	}

	@Override
	public List<Payment> findByStatusAndCreatedAtBetween(PayStatus status, LocalDateTime start, LocalDateTime end) {
		return List.of();
	}

}
