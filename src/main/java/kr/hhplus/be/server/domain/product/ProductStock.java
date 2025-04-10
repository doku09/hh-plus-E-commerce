package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
public class ProductStock {

	@Setter
	private long id;
	private int quantity;
	private Product product;

	public ProductStock(int quantity, Product product) {
		this.quantity = quantity;
		this.product = product;
	}

	public static ProductStock createInit(Product product) {
		int INIT_VALUE = 0;
		return new ProductStock(INIT_VALUE,product);
	}

	//TODO 테스트
	public void deduct(int amount) {
		if(this.quantity - amount < 0) throw new GlobalBusinessException(ErrorCode.NOT_ENOUGH_STOCK);

		this.quantity = this.quantity - amount;
	}
}
