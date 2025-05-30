package kr.hhplus.be.server.domain.product;

import jakarta.persistence.OptimisticLockException;
import kr.hhplus.be.server.common.TimeHelper;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.common.lock.aop.DistributedLockTransaction;
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

	public Product findById(long id) {
		return productRepository.findById(id).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_PRODUCT));
	}

	/**
	 * 상품 재고를 차감한다.
	 */
	@Retryable(retryFor = {
		OptimisticLockException.class,
		StaleObjectStateException.class,
		ObjectOptimisticLockingFailureException.class
	}, maxAttempts = 5, backoff = @Backoff(delay = 100))
	@Transactional
	public ProductInfo.OrderProducts deductOrderItemsStock(ProductCommand.OrderProducts orderProducts) {

		timeHelper.printTime();

		List<ProductInfo.OrderProduct> orderProductList = new ArrayList<>();

		for (ProductCommand.OrderProduct orderProduct : orderProducts.getOrderProducts()) {

			this.deductStock(ProductCommand.DeductStock.of(orderProduct.getProductId(),orderProduct.getQuantity()));

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
	@Retryable(retryFor = {
		OptimisticLockException.class,
		StaleObjectStateException.class,
		ObjectOptimisticLockingFailureException.class
	}, maxAttempts = 5, backoff = @Backoff(delay = 100))
	@Transactional
	public void deductStock(ProductCommand.DeductStock command) {
		ProductStock stock = stockRepository.findByProductId(command.getProductId());
		stock.deduct(command.getQuantity());
	}

	@DistributedLockTransaction(key = "#lockName.concat(':').concat(#command.getProductId())")
	public void deductStockWithAopLock(String lockName,ProductCommand.DeductStock command) {
		ProductStock stock = stockRepository.findByProductId(command.getProductId());
		stock.deduct(command.getQuantity());
	}

}
