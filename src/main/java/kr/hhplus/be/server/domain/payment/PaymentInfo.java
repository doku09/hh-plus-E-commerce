package kr.hhplus.be.server.domain.payment;

import lombok.Getter;

import java.util.List;

public class PaymentInfo {

	@Getter
	public static class History {

		private List<Payment> payments;

		private History(List<Payment> payments) {
			this.payments = payments;
		}

		public static History of(List<Payment> payments) {
			return new History(payments);
		}
	}

	@Getter
	public static class Payment {
		private Long orderId;
		private Long payPrice;

		private Payment(Long orderId, Long payPrice) {
			this.orderId = orderId;
			this.payPrice = payPrice;
		}

		public static Payment of(Long orderId, Long payPrice) {
			return new Payment(orderId, payPrice);
		}
	}
}
