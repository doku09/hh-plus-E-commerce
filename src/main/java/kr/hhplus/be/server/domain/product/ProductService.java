package kr.hhplus.be.server.domain.product;

public class ProductService {


	public void register(ProductCreateCommand command) {
		Product product = Product.create(
			command.name(),
			command.price()
		);
	}
}
