package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.order.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OrderResponse {

	@Getter
	@NoArgsConstructor
	public static class Order {

			private Long id;
			private Long totalPrice;
			private OrderStatus orderStatus;

			private Order(Long id, Long totalPrice, OrderStatus orderStatus) {
				this.id = id;
				this.totalPrice = totalPrice;
				this.orderStatus = orderStatus;
			}

			public static Order of(Long id, Long totalPrice, OrderStatus orderStatus) {
				return new Order(id,totalPrice,orderStatus);
		}

	}
}
