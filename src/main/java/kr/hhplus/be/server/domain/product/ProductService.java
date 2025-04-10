package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public void register(ProductCreateCommand command) {

		Product product = Product.create(
			command.name(),
			command.price()
		);

		ProductStock stock = ProductStock.createInit(product);

		// TODO QUESTION) Product + stock 저장시 ProductRepo하나만 쓰면 될까요?
		productRepository.save(product);
		productRepository.saveStock(stock);
	}

	public ProductInfo findById(long id) {

		Product product = productRepository.findById(id).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_PRODUCT));

		// TODO QUESTION 객체지향 생활체조에서 도메인에는 Getter,Setter를 지양하라고 했는데 이렇게 DTO를 만들어야할경우에는 어떻게 해야할까요?
		return ProductInfo.of(
			product.getId(),
			product.getName(),
			product.getPrice()
		);
	}
}
