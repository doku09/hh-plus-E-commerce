package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

public class OrderResult {

	@Getter
	public static class Order {

		private Long id;
		private Long totalPrice;
		private Long discountPrice;
		private OrderStatus orderStatus;

		private Order(Long id, Long totalPrice, Long discountPrice, OrderStatus orderStatus) {
			this.id = id;
			this.totalPrice = totalPrice;
			this.discountPrice = discountPrice;
			this.orderStatus = orderStatus;
		}

		public static Order of(Long id, Long totalPrice, Long discountPrice, OrderStatus orderStatus) {
			return new Order(id,totalPrice, discountPrice, orderStatus);
		}
	}



}
