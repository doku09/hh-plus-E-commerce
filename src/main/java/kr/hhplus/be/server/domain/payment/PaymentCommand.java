package kr.hhplus.be.server.domain.payment;

import lombok.Getter;

public class PaymentCommand {

	@Getter
	public static class Create {
		private Long orderId;
		private Long payPrice;

		public Create(Long orderId, Long payPrice) {
			this.orderId = orderId;
			this.payPrice = payPrice;
		}

		public static Create of(Long orderId, Long payPrice) {
			return new Create(orderId, payPrice);
		}
	}
}
