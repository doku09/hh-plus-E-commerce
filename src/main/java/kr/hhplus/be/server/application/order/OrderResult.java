package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;

public class OrderResult {

	@Getter
	public static class Order {

		private long id;
		private int totalPrice;
		private OrderStatus orderStatus;

		private Order(long id, int totalPrice, OrderStatus orderStatus) {
			this.id = id;
			this.totalPrice = totalPrice;
			this.orderStatus = orderStatus;
		}

		public static Order of(long id, int totalPrice, OrderStatus orderStatus) {
			return new Order(id,totalPrice,orderStatus);
		}
	}



}
