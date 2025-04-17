package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.OrderCommand;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderCriteria {

	@Getter
	public static class CreateOrder {
		private List<OrderItem> orderItems;
		private Long userId;
		private Long couponId;

		public CreateOrder(long userId,Long couponId, List<OrderItem> orderItems) {
			this.userId = userId;
			this.couponId = couponId;
			this.orderItems = orderItems;
		}

		public static CreateOrder of(long userId, Long couponId, List<OrderItem> orderProducts) {
			return new CreateOrder(userId,couponId, orderProducts);
		}
	}

	@Getter
	public static class OrderItem {
		private long productId;
		private int quantity;

		private OrderItem(long productId, int quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}

		public static OrderItem of(long productId, int quantity) {
			return new OrderItem(productId, quantity);
		}
	}
}
