package kr.hhplus.be.server.domain.bestItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.application.bestItem.BestItemsCacheDto;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderFixture;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
class BestItemServiceIntegrationTest {


	@Autowired private BestItemService bestItemService;
	@Autowired private BestItemRepository bestItemRepository;

	@Autowired private ProductRepository productRepository;
	@Autowired private OrderRepository orderRepository;
	@Autowired private ProductStockRepository stockRepository;

	@Test
	@DisplayName("[성공] 인기조회 상품을 캐시로 조회한다.")
	void cache_view_best_items() {

		// given
		List<Product> products = createProduct();
		createOrder();
		for (Product product : products) {
			createBestItems(product);
		}

		// when
		List<BestItemsCacheDto> items = bestItemService.getTop10BestItems();
		System.out.println("DB 조회: " + items.size());
		List<BestItemsCacheDto> items2 = bestItemService.getTop10BestItems();
		System.out.println("캐시 조회: " + items2.size());
		// then
		assertThat(items).isNotNull();
		assertThat(items.size()).isGreaterThan(1);
	}


	private void createBestItems(Product product) {
		bestItemRepository.save(BestItem.create(product,2));
	}

	private void createOrder() {
		Order order1 = OrderFixture.createOrderWithOrderItems(1L, OrderStatus.PAID);
		Order order2 = OrderFixture.createOrderWithOrderItems(2L,OrderStatus.PAID);
		Order order3 = OrderFixture.createOrderWithOrderItems(3L,OrderStatus.PAID);

		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
	}

	private List<Product> createProduct() {
		Product product1 = Product.create("뽕따", 1000L);
		Product product2 = Product.create("빠삐코", 1000L);
		Product product3 = Product.create("설레임", 1000L);
		productRepository.save(product1);
		productRepository.save(product2);
		productRepository.save(product3);
		stockRepository.save(ProductStock.createInit(product1.getId(), 5));
		stockRepository.save(ProductStock.createInit(product2.getId(), 5));
		stockRepository.save(ProductStock.createInit(product3.getId(), 5));

		return List.of(product1,product2,product3);
	}


}