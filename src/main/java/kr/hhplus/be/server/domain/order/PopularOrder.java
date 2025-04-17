package kr.hhplus.be.server.domain.order;

import lombok.Getter;

@Getter
public class PopularOrder {
	private Long itemId;
	private Integer orderQuantity;


	public PopularOrder(Long itemId, Integer orderQuantity) {
		this.itemId = itemId;
		this.orderQuantity = orderQuantity;
	}
}
