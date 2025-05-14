package kr.hhplus.be.server.application.bestItem;

import kr.hhplus.be.server.domain.bestItem.BestItem;
import lombok.Getter;

@Getter
public class BestItemsCacheDto {
	private Long productId;
	private String productName;
	private Long price;
	private Long salesCount;

	private BestItemsCacheDto(Long productId, String productName, Long price, Long salesCount) {
		this.productId = productId;
		this.productName = productName;
		this.price = price;
		this.salesCount = salesCount;
	}

	public static BestItemsCacheDto from(BestItem item) {
		return new BestItemsCacheDto(
			item.getProductId(),
			item.getProductName(),
			item.getPrice(),
			item.getSalesCount()
		);
	}
}
