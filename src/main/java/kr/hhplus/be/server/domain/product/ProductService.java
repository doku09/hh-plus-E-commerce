package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public ProductInfo.Product register(ProductCommand.Create command) {

		Product product = Product.create(
			command.getName(),
			command.getPrice()
		);

		ProductStock stock = ProductStock.createInit(product,command.getQuantity());

		productRepository.save(product);
		productRepository.saveStock(stock);

		return ProductInfo.Product.of(
			product.getId(),
			product.getName(),
			product.getPrice()
		);
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

	public ProductInfo.OrderProducts deductStock(ProductCommand.OrderProducts orderProducts) {

		List<ProductInfo.OrderProduct> orderProductList = new ArrayList<>();

		for (ProductCommand.OrderProduct orderProduct : orderProducts.getOrderProducts()) {
			ProductStock productStock = productRepository.findStockByProductId(orderProduct.getProductId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_STOCK));

			// 재고 차감
			productStock.deduct(orderProduct.getQuantity());

			Product product = productRepository.findById(orderProduct.getProductId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_PRODUCT));

			orderProductList.add(ProductInfo.OrderProduct.of(product.getId(), product.getName(), product.getPrice(), productStock.getQuantity()));
		}
		return ProductInfo.OrderProducts.of(orderProductList);
	}

	public ProductInfo.TopProducts getProductsByIds(List<Long> list) {
		List<Product> products = productRepository.findProductsByIds(list);

		List<ProductInfo.Product> resultInfo = products.stream().map(product -> ProductInfo.Product.of(product.getId(), product.getName(), product.getPrice())).toList();

		return ProductInfo.TopProducts.of(resultInfo);
	}
}
