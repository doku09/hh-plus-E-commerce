package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.stock.ProductStockJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
class ProductServiceIntegrationTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductStockRepository stockRepository;
	@Autowired
	private ProductRepository productRepository;


	@Autowired
	private ProductJpaRepository productJpaRepository;
	@Autowired
	private ProductStockJpaRepository productStockJpaRepository;

	@AfterEach
	void tearDown() {
		productJpaRepository.deleteAllInBatch();
		productStockJpaRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("[성공] 상품을 등록한다.")
	void registerProduct_success() {

	  // given
		ProductCommand.Create command = ProductCommand.Create.of("당근", 1000L, 0);

		// when
		productService.register(command);

		//then
		productRepository.findById(1L)
			.ifPresent(product -> {
				assertThat(product.getName()).isEqualTo("당근");
				assertThat(product.getPrice()).isEqualTo(1000L);
			});

	}

	@Test
	@DisplayName("[성공] 주문 성공시 재고차감")
	void success_order_deductStock() {

	  // given

		// 1번 1개, 2번 1개
		// when
		Product p1 = Product.create("수박", 3000L);
		productRepository.save(p1);
		stockRepository.save(ProductStock.createInit(p1.getId(),10));

		Product p2 = Product.create("사과", 3000L);
		productRepository.save(p2);
		stockRepository.save(ProductStock.createInit(p2.getId(),5));

		ProductCommand.OrderProducts orderProducts = ProductCommand.OrderProducts.of(List.of(
			ProductCommand.OrderProduct.of(p1.getId(), 5),
			ProductCommand.OrderProduct.of(p2.getId(), 5))
		);

		ProductInfo.OrderProducts products = productService.deductOrderItemsStock(orderProducts);

	  // then
		Long findP1 = products.getOrderProducts().get(0).getProductId();
		Long findP2 = products.getOrderProducts().get(1).getProductId();

		ProductStock stock1 = stockRepository.findByProductId(findP1);
		ProductStock stock2 = stockRepository.findByProductId(findP2);
		assertThat(stock1.getQuantity()).isEqualTo(5);
		assertThat(stock2.getQuantity()).isEqualTo(0);
	}
}