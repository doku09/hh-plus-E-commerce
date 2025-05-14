package kr.hhplus.be.server.application.bestItem;

import kr.hhplus.be.server.domain.bestItem.BestItem;
import kr.hhplus.be.server.domain.bestItem.BestItemRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderFixture;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import kr.hhplus.be.server.infrastructure.order.OrderItemJpaRepository;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import kr.hhplus.be.server.infrastructure.product.ProductJpaRepository;
import kr.hhplus.be.server.infrastructure.stock.ProductStockJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
class BestItemSchedulerTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductStockRepository stockRepository;

	@Autowired
	private BestItemScheduler scheduler;

	@Autowired
	private BestItemRepository bestItemRepository;
	@Autowired
	private ProductJpaRepository productJpaRepository;
	@Autowired
	private ProductStockJpaRepository productStockJpaRepository;
	@Autowired
	private OrderJpaRepository orderJpaRepository;
	@Autowired
	private OrderItemJpaRepository orderItemJpaRepository;

	@BeforeEach
	void setUp() {
		orderItemJpaRepository.deleteAllInBatch();
		orderJpaRepository.deleteAllInBatch();

		productJpaRepository.deleteAllInBatch();
		productStockJpaRepository.deleteAllInBatch();
	}

	@Test
	@DisplayName("[성공] 한시간전부터 판매된 내역을 조회한다.")
	void beforeoneHour_sales() {

	  // given
//		createProduct();
		Product product1 = Product.create("뽕따", 1000L);
		Product product2 = Product.create("빠삐코", 1000L);
		Product product3 = Product.create("설레임", 1000L);
		productRepository.save(product1);
		productRepository.save(product2);
		productRepository.save(product3);
		stockRepository.save(ProductStock.createInit(product1.getId(), 5));
		stockRepository.save(ProductStock.createInit(product2.getId(), 5));
		stockRepository.save(ProductStock.createInit(product3.getId(), 5));

//		createOrder();
		Order order1 = OrderFixture.createOrderWithOrderItems(product1.getId(),1L, OrderStatus.PAID);
		Order order2 = OrderFixture.createOrderWithOrderItems(product2.getId(),2L,OrderStatus.PAID);
		Order order3 = OrderFixture.createOrderWithOrderItems(product3.getId(),3L,OrderStatus.PAID);

		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
		// when
		scheduler.warmDailyRanking();

	  // then
		List<BestItem> items = bestItemRepository.findBestItemsTopCount(LocalDateTime.now().minusHours(1), 5);

		assertThat(items).isNotNull();
		assertThat(items.size()).isGreaterThan(1);
		assertThat(items.get(0).getProductId()).isEqualTo(1L);
	}

	private void createOrder() {
		Order order1 = OrderFixture.createOrderWithOrderItems(1L, OrderStatus.PAID);
		Order order2 = OrderFixture.createOrderWithOrderItems(2L,OrderStatus.PAID);
		Order order3 = OrderFixture.createOrderWithOrderItems(3L,OrderStatus.PAID);

		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
	}

	private void createProduct() {
		Product product1 = Product.create("뽕따", 1000L);
		Product product2 = Product.create("빠삐코", 1000L);
		Product product3 = Product.create("설레임", 1000L);
		productRepository.save(product1);
		productRepository.save(product2);
		productRepository.save(product3);
		stockRepository.save(ProductStock.createInit(product1.getId(), 5));
		stockRepository.save(ProductStock.createInit(product2.getId(), 5));
		stockRepository.save(ProductStock.createInit(product3.getId(), 5));
	}

}