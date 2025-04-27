package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.application.product.RedissonLockStockFacade;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceRedisLockTest {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductStockRepository stockRepository;
	@Autowired
	private ConcurrencyExecutor executor;
	@Autowired
	private RedissonLockStockFacade stockFacade;

	@Test
	@DisplayName("[성공] pub/sub 방식을 적용하여 재고처리를 순차적으로 진행한다.")
	void redis_pubsub_deduct_stock() {

	  // given
		Product product = Product.create("스테이크", 10_000L);
		productRepository.save(product);

		ProductStock stock = stockRepository.save(ProductStock.createInit(product.getId(), 10));

		ProductCommand.DeductStock deductStock = ProductCommand.DeductStock.of(product.getId(), 1);

		// when
		executor.execute(() -> {
			stockFacade.deductStock(deductStock);
		}, 5);

	  // then
		ProductStock result = stockRepository.findByProductId(product.getId());

		assertThat(result.getQuantity()).isEqualTo(5);
	}
}
