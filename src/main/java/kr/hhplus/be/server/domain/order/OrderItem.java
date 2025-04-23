package kr.hhplus.be.server.domain.order;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private Long productId;
	private Long productPrice;
	private Long totalPrice;
	private int quantity;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id")
	private Order order;


	private OrderItem(Long productId, Long productPrice, int quantity,  Long totalPrice) {
		this.productId = productId;
		this.productPrice = productPrice;
		this.quantity = quantity;
		this.totalPrice = totalPrice;
	}

	public static OrderItem of(Long productId,Long productPrice, int quantity) {
		Long totalPrice = productPrice * quantity;
		return new OrderItem(productId,productPrice, quantity, totalPrice);
	}

	public Long getTotalPrice() {
		return productPrice * quantity;
	}

	public void assignOrder(Order order) {
		this.order = order;
	}
}
