package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.BaseTimeEntity;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//TODO QUESTION Getter 지양인데 OrderResponse를 바인딩해줘야되는데 만들어야 할까요?
@Getter
public class Order extends BaseTimeEntity {

	private Long id;
	private long totalPrice;
	private Long discountPrice;
	private OrderStatus status;
	private final Long userId;
	private Long couponId;
	private List<OrderItem> orderItems = new ArrayList<>();

	public Order(Long userId) {
		this.userId = userId;
	}

	public static Order createOrder(Long userId) {
		return new Order(userId);
	}

	public void applyCoupon(Coupon coupon) {
		this.couponId = coupon.getId();
		this.discountPrice -= coupon.getDiscountPrice();
	}

	public void addItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
		orderItem.assignOrder(this);
		totalPrice += orderItem.getTotalPrice();
	}

	public void changeStatus(OrderStatus orderStatus) {
		this.status = orderStatus;
	}
}
