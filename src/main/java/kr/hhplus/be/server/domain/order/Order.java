package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseTimeEntity;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "orders")
public class Order extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Long id;

	private long totalPrice;

	private Long discountPrice;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	private Long userId;

	private Long couponId;

	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<OrderItem> orderItems = new ArrayList<>();

	public Order(Long userId) {
		this.status = OrderStatus.ORDERED;
		this.userId = userId;
	}

	public Order(Long userId, OrderStatus status) {
		this.userId = userId;
		this.status = OrderStatus.PAID;
	}

	public static Order createOrder(Long userId) {
		return new Order(userId);
	}

	public void applyCoupon(Long couponId,Long discountPrice) {
		if(this.couponId != null) {
			this.couponId = couponId;
			// 할인가격이 최종가격 discount가 0이면 그냥 totalPrice
			this.discountPrice = totalPrice - discountPrice;
		}
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
