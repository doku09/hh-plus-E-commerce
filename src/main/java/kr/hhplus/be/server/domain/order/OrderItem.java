package kr.hhplus.be.server.domain.order;

import lombok.Getter;

@Getter
public class OrderItem {

	private long id;
	private Long productId;
	private int productPrice;
	private int quantity;
	private Order order;

	private OrderItem(Long productId, int productPrice, int quantity,  int totalPrice) {
		this.productId = productId;
		this.productPrice = productPrice;
		this.quantity = quantity;
	}

	public static OrderItem of(Long productId,int productPrice, int quantity) {
		int totalPrice = productPrice * quantity;
		return new OrderItem(productId,productPrice, quantity, totalPrice);
	}

	public int getItemsPrice() {
		return productPrice * quantity;
	}

	public int getTotalPrice() {
		return productPrice * quantity;
	}

	public void assignOrder(Order order) {
		this.order = order;
	}
}
