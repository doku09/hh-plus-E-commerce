package kr.hhplus.be.server.domain.product;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProductInfo {

	private long id;
	private String name;
	private int price;

	public static ProductInfo of(long id, String name, int price) {
		return ProductInfo.builder()
			.id(id)
			.name(name)
			.price(price)
			.build();
	}
}
