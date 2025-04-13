package kr.hhplus.be.server.domain.order;


import lombok.Getter;

import java.time.LocalDateTime;

public class OrderInfo {

	@Getter
	public static class Order {

		private long id;
		private int totalPrice;
		private OrderStatus status;
		private long userId;
		private LocalDateTime createdAt;

		private Order(long id, int totalPrice, OrderStatus status) {
			this.id = id;
			this.totalPrice = totalPrice;
			this.status = status;
		}

		public static OrderInfo.Order of(long id, int totalPrice, OrderStatus status) {
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
