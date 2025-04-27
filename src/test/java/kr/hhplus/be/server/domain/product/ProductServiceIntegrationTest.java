package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@SpringBootTest
class ProductServiceIntegrationTest {

	@Autowired
	private ProductService productService;
	@Autowired
	private ProductStockRepository stockRepository;
	@Autowired
	private ProductRepository productRepository;


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
		ProductInfo.Product p1 = productService.register(ProductCommand.Create.of("수박", 3000L, 10));
		ProductInfo.Product p2 = productService.register(ProductCommand.Create.of("사과", 4500L, 5));

		ProductCommand.OrderProducts orderProducts = ProductCommand.OrderProducts.of(List.of(
			ProductCommand.OrderProduct.of(p1.getId(), 5),
			ProductCommand.OrderProduct.of(p2.getId(), 5))
		);

		ProductStock productStock1 = stockRepository.findByProductId(p1.getId());
		ProductStock productStock2 = stockRepository.findByProductId(p2.getId());

		Product findProduct1 = productRepository.findById(p1.getId()).orElse(null);
		Product findProduct2 = productRepository.findById(p2.getId()).orElse(null);

		ProductInfo.OrderProducts products = productService.deductOrderItemsStock(orderProducts);

	  // then
		assertThat(products.getOrderProducts().get(0).getQuantity()).isEqualTo(5);
		assertThat(products.getOrderProducts().get(1).getQuantity()).isEqualTo(0);
	}
}