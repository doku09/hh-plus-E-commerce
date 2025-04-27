package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.domain.product.ProductCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductRequest {

	@Getter
	@NoArgsConstructor
	public static class Create {
		private String name;
		private Long price;
		private int quantity;

		private Create(String name, Long price,int quantity) {
			this.name = name;
			this.price = price;
			this.quantity = quantity;
		}

		public static Create of(String name, Long price,int quantity) {
			return new Create(name, price, quantity);
		}

		public ProductCommand.Create toCommand() {
			return ProductCommand.Create.of(name,price,quantity);
		}
	}
}
