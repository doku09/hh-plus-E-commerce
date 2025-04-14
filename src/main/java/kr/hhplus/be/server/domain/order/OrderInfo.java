package kr.hhplus.be.server.domain.order;


import lombok.Getter;

import java.time.LocalDateTime;

public class OrderInfo {

	@Getter
	public static class Order {

		private Long id;
		private Long totalPrice;
		private OrderStatus status;
		private Long userId;
		private LocalDateTime createdAt;

		private Order(Long id, Long totalPrice, OrderStatus status) {
			this.id = id;
			this.totalPrice = totalPrice;
			this.status = status;
		}

		public static OrderInfo.Order of(Long id, Long totalPrice, OrderStatus status) {
			return new Order(id,totalPrice,status);
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
}
