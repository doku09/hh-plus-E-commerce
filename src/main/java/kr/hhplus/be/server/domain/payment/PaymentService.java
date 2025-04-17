package kr.hhplus.be.server.domain.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;

	public void pay(PaymentCommand.Create createCommand) {

		Payment payment = Payment.completePay(createCommand.getOrderId(), createCommand.getPayPrice());

		paymentRepository.save(payment);
	}

	public PaymentInfo.History getPaymentHistoryWithPaid(LocalDateTime startDate, LocalDateTime endDate) {

		List<Payment> histories = paymentRepository.getPaymentHistoryWithPaid(startDate, endDate,PayStatus.COMPLETE);
		return PaymentInfo.History.of(histories.stream().map(his -> PaymentInfo.Payment.of(his.getOrderId(), his.getPayPrice())).toList());
	}
}
