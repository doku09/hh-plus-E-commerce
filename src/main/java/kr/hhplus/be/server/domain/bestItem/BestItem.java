package kr.hhplus.be.server.domain.bestItem;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.domain.BaseTimeEntity;
import kr.hhplus.be.server.domain.product.Product;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
public class BestItem extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long productId;
	private String productName;
	private Long price;
	private Long salesCount;

	private BestItem(Long productId, String productName, Long stock, Long salesCount) {
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.salesCount = salesCount;
	}

	public static BestItem create(Product product, long salesCount) {
		return new BestItem(product.getId(), product.getName(), product.getPrice(), salesCount);
	}

	public void addSalesCount(Long salesQuantity) {
		this.salesCount += salesQuantity;
	}
}
