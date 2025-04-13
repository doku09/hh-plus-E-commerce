package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public void register(ProductCommand.Create command) {

		Product product = Product.create(
			command.getName(),
			command.getPrice()
		);

		ProductStock stock = ProductStock.createInit(product);

		productRepository.save(product);
		productRepository.saveStock(stock);
	}

	public ProductInfo.Product findById(long id) {

		Product product = productRepository.findById(id).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_PRODUCT));

		// TODO QUESTION 객체지향 생활체조에서 도메인에는 Getter,Setter를 지양하라고 했는데 이렇게 DTO를 만들어야할경우에는 어떻게 해야할까요?
		return ProductInfo.Product.of(
			product.getId(),
			product.getName(),
			product.getPrice()
		);
	}

	public ProductStock findStockByProductId(long id) {
		return productRepository.findStockByProductId(id).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_STOCK));
	}

	public void deductStock(ProductStockCommand.Deduct command) {
		ProductStock productStock = productRepository.findStockByProductId(command.getProductId()).orElseThrow(() -> new IllegalArgumentException("상품 재고가 존재하지 않습니다."));

		productStock.deduct(command.getQuantity());
	}
}
