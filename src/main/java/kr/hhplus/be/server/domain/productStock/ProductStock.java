package kr.hhplus.be.server.domain.productStock;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ProductStock {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private int quantity;

	@Version
	private Long version;

	private Long productId;

	public ProductStock(Long productId,int quantity) {
		this.quantity = quantity;
		this.productId = productId;
	}

	public static ProductStock createInit(Long productId, int quantity) {
		return new ProductStock(productId,quantity);
	}

	public void deduct(int amount) {
		if(this.quantity - amount < 0) throw new GlobalBusinessException(ErrorCode.NOT_ENOUGH_STOCK);

		this.quantity = this.quantity - amount;
	}
}
