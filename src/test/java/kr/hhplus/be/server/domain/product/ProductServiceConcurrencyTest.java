package kr.hhplus.be.server.domain.product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.common.exception.OptimisticLockingRetryException;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.ActiveProfiles;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

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

	@PersistenceContext
	private EntityManager em;

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

}
