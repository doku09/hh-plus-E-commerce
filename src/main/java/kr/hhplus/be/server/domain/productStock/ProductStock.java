package kr.hhplus.be.server.domain.productStock;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.product.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
public class ProductStock {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int quantity;
	@OneToOne
	@JoinColumn(name = "product_id")
	private Product product;

	public ProductStock(Product product,int quantity) {
		this.quantity = quantity;
		this.product = product;
	}

	public static ProductStock createInit(Product product, int quantity) {
		return new ProductStock(product,quantity);
	}

	public void deduct(int amount) {
		if(this.quantity - amount < 0) throw new GlobalBusinessException(ErrorCode.NOT_ENOUGH_STOCK);

		this.quantity = this.quantity - amount;
	}
}
