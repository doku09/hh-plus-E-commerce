package kr.hhplus.be.server.domain.product;

import lombok.Builder;

@Builder
public record ProductCreateCommand(String name, int price) {

	public Product toEntity() {
		return Product.create(name,price);
	}
}
