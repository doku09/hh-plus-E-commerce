package kr.hhplus.be.server.domain.order;

import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class OrderCommand {

	@Getter
	public static class Create {
		private final Long userId;
		private OrderStatus orderStatus;
		private List<OrderItem> orderItems;
		private Long totalPrice;
		@Nullable private Long couponId;
		private Long discountPrice;

		private Create(Long userId,Long couponId, List<OrderItem> orderItems) {
			this.orderStatus = OrderStatus.ORDERED;
			this.userId = userId;
			this.couponId = couponId;
			this.orderItems = orderItems;
		}

		public static Create of(Long userId,Long couponId, List<OrderItem> orderItems) {
			return new Create(userId, couponId, orderItems);
		}

		public static Create of(Long userId, Long couponId) {
			return new Create(userId, couponId, new ArrayList<>());
		}
	}

	@Getter
	public static class OrderItem {
		private final long productId;
		private final int productPrice;
		private final int quantity;

		private OrderItem(long productId, int productPrice, int quantity) {
			this.productPrice = productPrice;
			this.productId = productId;
			this.quantity = quantity;
		}

		public static OrderItem of(long productId, int productPrice, int quantity) {
			return new OrderItem(productId, productPrice, quantity);
		}

		public int getTotalPrice() {
			return productPrice * quantity;
		}
	}
}
