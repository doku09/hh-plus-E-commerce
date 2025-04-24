package kr.hhplus.be.server.domain.product;

import jakarta.persistence.OptimisticLockException;
import kr.hhplus.be.server.common.TimeHelper;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;
	private final ProductStockRepository stockRepository;
	private final TimeHelper timeHelper;

	@Transactional
	public ProductInfo.Product register(ProductCommand.Create command) {

		Product product = Product.create(
			command.getName(),
			command.getPrice()
		);


		productRepository.save(product);
		ProductStock stock = ProductStock.createInit(product.getId(),command.getQuantity());
		ProductStock savedStock = stockRepository.save(stock);

		return ProductInfo.Product.of(
			product.getId(),
			product.getName(),
			product.getPrice()
		);
	}

	public ProductInfo.Product findById(long id) {

		Product product = productRepository.findById(id).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_PRODUCT));

		return ProductInfo.Product.of(
			product.getId(),
			product.getName(),
			product.getPrice()
		);
	}

	/**
	 * 상품 재고를 차감한다.
	 */
	@Retryable(retryFor = {
		OptimisticLockException.class,
		StaleObjectStateException.class,
		ObjectOptimisticLockingFailureException.class
	}, maxAttempts = 5, backoff = @Backoff(delay = 100))
	public ProductInfo.OrderProducts deductOrderItemsStock(ProductCommand.OrderProducts orderProducts) {

		timeHelper.printTime();

		List<ProductInfo.OrderProduct> orderProductList = new ArrayList<>();

		for (ProductCommand.OrderProduct orderProduct : orderProducts.getOrderProducts()) {

			ProductStock productStock = stockRepository.findByProductId(orderProduct.getProductId());

			// 재고 차감
			productStock.deduct(orderProduct.getQuantity());

			Product product = productRepository.findById(orderProduct.getProductId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_PRODUCT));

			orderProductList.add(
				ProductInfo.OrderProduct.of(product.getId(),
				product.getName(),
				product.getPrice(),
				orderProduct.getQuantity())
			);
		}

		return ProductInfo.OrderProducts.of(orderProductList);
	}

	public ProductInfo.TopProducts getProductsByIds(List<Long> list) {
		List<Product> products = productRepository.findProductsByIds(list);

		List<ProductInfo.Product> resultInfo = products.stream().map(product -> ProductInfo.Product.of(product.getId(), product.getName(), product.getPrice())).toList();

		return ProductInfo.TopProducts.of(resultInfo);
	}

	/**
	 * 상품 재고를 차감한다.
	 */
//	@Retryable(retryFor = {
//		OptimisticLockException.class,
//		StaleObjectStateException.class,
//		ObjectOptimisticLockingFailureException.class
//	}, maxAttempts = 5, backoff = @Backoff(delay = 100))
	@Transactional
	public void deductStock(ProductCommand.DeductStock command) {
		ProductStock stock = stockRepository.findByProductId(command.getProductId());
		stock.deduct(command.getQuantity());
	}
}
