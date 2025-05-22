package kr.hhplus.be.server.application.order;

import lombok.Getter;

@Getter
public class OrderItemDto {

	private Long id;
	private Long productId;
	private int quantity;
	private Long productPrice;
	private Long totalPrice;

	public OrderItemDto() {
	}

	public OrderItemDto(Long id, Long productId, int quantity, Long productPrice, Long totalPrice) {
		this.id = id;
		this.productId = productId;
		this.quantity = quantity;
		this.productPrice = productPrice;
		this.totalPrice = totalPrice;
	}
}
