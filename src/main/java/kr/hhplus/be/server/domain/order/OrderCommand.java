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

		private Create(Long userId,Long couponId,Long discountPrice, List<OrderItem> orderItems) {
			this.orderStatus = OrderStatus.ORDERED;
			this.userId = userId;
			this.discountPrice = discountPrice;
			this.couponId = couponId;
			this.orderItems = orderItems;
		}

		public static Create of(Long userId,Long couponId,Long discountPrice, List<OrderItem> orderItems) {
			return new Create(userId, couponId,discountPrice, orderItems);
		}
	}

	@Getter
	public static class OrderItem {
		private final Long productId;
		private final Long productPrice;
		private final int quantity;

		private OrderItem(Long productId, Long productPrice, int quantity) {
			this.productPrice = productPrice;
			this.productId = productId;
			this.quantity = quantity;
		}

		public static OrderItem of(Long productId, Long productPrice, int quantity) {
			return new OrderItem(productId, productPrice, quantity);
		}

		public Long getTotalPrice() {
			return productPrice * quantity;
		}
	}

	@Getter
	public static class TopOrderedProducts {
		private List<Long> orderIds;
		private Integer limit;

		private TopOrderedProducts(List<Long> orderIds,Integer limit) {
			this.orderIds = orderIds;
			this.limit = limit;
		}

		public static TopOrderedProducts of(List<Long> orderIds,Integer limit) {
			return new TopOrderedProducts(orderIds, limit);
		}
	}

}
