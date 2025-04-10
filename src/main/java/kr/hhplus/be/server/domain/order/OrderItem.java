package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;

public class OrderItem {

	private long id;
	private Product product;
	private int quantity;
	@Getter
	private int totalPrice;
	private Order order;

	private OrderItem(Product product, int quantity, int totalPrice) {
		this.product = product;
		this.quantity = quantity;
	}

	public static OrderItem of(Product product, int quantity) {
		int totalPrice = product.getPrice() * quantity;
		return new OrderItem(product, quantity, totalPrice);
	}

	public void assignOrder(Order order) {
		this.order = order;
	}
}
