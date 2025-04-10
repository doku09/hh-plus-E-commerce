package kr.hhplus.be.server.application.order;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderCriteria {

	private List<OrderProduct> orderProducts;
	private long couponId;
	private long userId;

	@Getter
	public static class OrderProduct {
		private long productId;
		private int quantity;
	}


}
