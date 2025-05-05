package kr.hhplus.be.server.domain.product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.common.exception.OptimisticLockingRetryException;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
public class ProductServiceConcurrencyTest {

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductStockRepository stockRepository;

	@Autowired
	private ConcurrencyExecutor executor;

	@Test
	@DisplayName("주문상품 차감 시 재고가 차감된다.")
	void deduct_orderITems() {
		Product product = Product.create("다진고기", 1000L);
		productRepository.save(product);
		productRepository.findById(product.getId()).orElseThrow();
		stockRepository.save(ProductStock.createInit(product.getId(), 5));

		ProductCommand.OrderProducts command = ProductCommand.OrderProducts.of(List.of(
			ProductCommand.OrderProduct.of(product.getId(), 1)
		));

		executor.execute(()->{
			try {
				productService.deductOrderItemsStock(command);
			} catch(Exception e) {
				System.out.println("충돌!!");
				System.out.println(e.getClass().getSimpleName());
				throw e;
			}
		}, 3);

		ProductStock stock = stockRepository.findByProductId(product.getId());

		assertThat(stock.getQuantity()).isEqualTo(2);
	}

	@Test
	@DisplayName("[성공] 동시에 재고차감 시 DB락으로 제어한다.")
	void concurrency_deduct_stock() {

	  // given

		// 상품 5개 생성
		ProductCommand.Create command = ProductCommand.Create.of("다진고기", 1000L, 5);

		Product product = Product.create(command.getName(),command.getPrice());
		productRepository.save(product);

		ProductStock beforeStock = ProductStock.createInit(product.getId(),command.getQuantity());
		stockRepository.save(beforeStock);

		ProductCommand.DeductStock stockCommand = ProductCommand.DeductStock.of(product.getId(), 1);

		// when
		AtomicInteger successCount = new AtomicInteger();

		// 동시에 3개의 스레드가 하나씩 재고 차감시도
		executor.execute(()->{
		try {
			productService.deductStock(stockCommand);
			successCount.incrementAndGet();
		} catch (ObjectOptimisticLockingFailureException e) {
			throw new OptimisticLockingRetryException();
		}},3);

		ProductStock stock = stockRepository.findByProductId(product.getId());

	  // then
		assertThat(stock).isNotNull();
		assertThat(stock.getQuantity()).isEqualTo(5-successCount.get());
	}

	@Test
	@DisplayName("[성공] AOP 분산락을 사용하여 재고차감 동시성문제를 해결한다.")
	void deduct_stock_aop_concurrency() {

	  // given
		Product product = Product.create("상품1",1000L);
		productRepository.save(product);

		ProductStock beforeStock = ProductStock.createInit(product.getId(),5);
		stockRepository.save(beforeStock);

	  // when
		AtomicInteger successCount = new AtomicInteger();

		executor.execute(()->{
			try {
				productService.deductStockWithAopLock("product",ProductCommand.DeductStock.of(product.getId(), 1));
				successCount.incrementAndGet();
			} catch (Exception e) {
				e.printStackTrace();
			}},5);

	  // then

		ProductStock stock = stockRepository.findByProductId(product.getId());
		// then
		assertThat(stock).isNotNull();
		assertThat(stock.getQuantity()).isEqualTo(5-successCount.get());

	}
}
