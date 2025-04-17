package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Payment extends BaseTimeEntity {

	@Column(name="payment_id")
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long orderId;
	private Long payPrice;
	private PayStatus status;

	public Payment(Long orderId, Long payPrice, PayStatus status) {
		this.status = status;
		this.orderId = orderId;
		this.payPrice = payPrice;
	}

	public static Payment completePay(Long orderId, Long payPrice) {
		return new Payment(orderId, payPrice, PayStatus.COMPLETE);
	}
}
