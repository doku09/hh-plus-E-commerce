package kr.hhplus.be.server.domain.order;


import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

public class OrderInfo {

	@Getter
	public static class Order {

		private Long id;
		private Long totalPrice;
		private Long discountPrice;

		private OrderStatus status;
		private Long userId;
		private LocalDateTime createdAt;

		private Order(Long id, Long totalPrice, Long discountPrice, OrderStatus status) {
			this.id = id;
			this.totalPrice = totalPrice;
			this.discountPrice = discountPrice;
			this.status = status;
		}

		public static OrderInfo.Order of(Long id, Long totalPrice,Long discountPrice, OrderStatus status) {
			return new Order(id,totalPrice,discountPrice, status);
		}
	}

	public static class OrderItem {
		private String productName;
		private int productPrice;
		private int quantity;
		private int totalPrice;

		private OrderItem(String productName, int productPrice, int quantity, int totalPrice) {
			this.productName = productName;
			this.productPrice = productPrice;
			this.quantity = quantity;
			this.totalPrice = totalPrice;
		}

		public OrderInfo.OrderItem of(String productName, int productPrice, int quantity, int totalPrice) {
			return new OrderItem(productName,productPrice,quantity,totalPrice);
		}
	}


	@Getter
	public static class TopOrder {
		private List<PopularOrder> popularOrders;

		private TopOrder(List<PopularOrder> popularOrders) {
			this.popularOrders = popularOrders;
		}

		public static TopOrder of(List<PopularOrder> popularOrders) {
			return new TopOrder(popularOrders);
		}
	}
}
