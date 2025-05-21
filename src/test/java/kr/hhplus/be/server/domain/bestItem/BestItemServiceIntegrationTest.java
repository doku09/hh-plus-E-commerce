package kr.hhplus.be.server.domain.bestItem;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import kr.hhplus.be.server.application.bestItem.BestItemScheduler;
import kr.hhplus.be.server.application.bestItem.BestItemsCacheDto;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderFixture;
import kr.hhplus.be.server.domain.order.OrderRepository;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import kr.hhplus.be.server.infrastructure.order.OrderJpaRepository;
import kr.hhplus.be.server.infrastructure.redis.RedisRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
@ActiveProfiles("test")
@SpringBootTest
class BestItemServiceIntegrationTest {


	@Autowired private BestItemService bestItemService;
	@Autowired private BestItemRepository bestItemRepository;

	@Autowired private ProductRepository productRepository;
	@Autowired private OrderRepository orderRepository;
	@Autowired private ProductStockRepository stockRepository;
	@Autowired private BestItemScheduler bestItemScheduler;
	@Autowired private OrderJpaRepository orderJpaRepository;
	@Autowired RedisTemplate<String, Object> redisTemplate;
	@Autowired RedisRepository redisRepository;

	DateTimeFormatter DAILY_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
	DateTimeFormatter WEEKLY_FORMAT = DateTimeFormatter.ofPattern("YYYY-ww");
	String DAILY_KEY_PREFIX = "ranking:daily:";
	String WEEK_KEY_PREFIX = "ranking:weekly:";

	@AfterEach
	void tearDown() {
		orderJpaRepository.deleteAllInBatch();
		Set<String> keys = redisTemplate.keys("*");
		if (keys != null && !keys.isEmpty()) {
			// 조회된 키들 한 번에 삭제
			redisTemplate.delete(keys);
		}
	}

	@Test
	@DisplayName("[성공] 인기상품을 캐시로 조회한다.")
	void cache_view_best_items() {

		// given
		List<Product> products = createProduct();

		// 상품1-10개
		// 상품2-9개
		// 상품3-8개
		long salesCount = 10;
		for (Product product : products) {
			createBestItems(product,salesCount);
			salesCount--;
		}

		// when
		List<BestItemsCacheDto> items = bestItemService.getTop10BestItems();
		System.out.println("DB 조회: " + items.size());

		List<BestItemsCacheDto> items2 = bestItemService.getTop10BestItems();
		System.out.println("캐시 조회: " + items2.size());
		// then
		assertThat(items).isNotNull();
		assertThat(items)
			.extracting(BestItemsCacheDto::getProductId)
			.containsExactly(1L,2L,3L);
	}

	@Test
	@DisplayName("일간 인기상품을 조회할 수 있다.")
	void get_daily_rank_item() {

	  // given
		String dailyKey = DAILY_KEY_PREFIX + LocalDate.now().format(DAILY_FORMAT);
		IntStream.range(1,10).forEach((i) -> {
			redisRepository.addSortedSetWithTTL(dailyKey,String.valueOf(i),i*10, Duration.ofHours(25));
		});

	  // when
		bestItemScheduler.warmDailyRanking();

	  // then
		Set<Object> result = bestItemService.getTop10DailyRank();
		assertThat(result).extracting(o -> ((String)o)).containsExactly("9", "8", "7", "6", "5", "4", "3", "2", "1");
	}

	@Test
	@DisplayName("주간 인기상품을 조회할 수 있다.")
	void get_weekly_rank_item() {

		// given
		String weekKey = WEEK_KEY_PREFIX + LocalDate.now().format(WEEKLY_FORMAT);
		IntStream.range(1,5).forEach((i) -> {
			redisRepository.addSortedSetWithTTL(weekKey,String.valueOf(i),i*10, Duration.ofHours(25));
		});

		// when
		bestItemScheduler.warmWeeklyRanking();

		// then
		Set<Object> result = bestItemService.getTop10WeeklyRank();
		assertThat(result).extracting(o -> ((String)o)).containsExactly("4", "3", "2", "1");
	}


	private void createBestItems(Product product,Long salesCount) {
		bestItemRepository.save(BestItem.create(product,salesCount));
	}

	private void createOrder() {
		Order order1 = OrderFixture.createOrderWithOrderItems(1L, OrderStatus.PAID);
		Order order2 = OrderFixture.createOrderWithOrderItems2(2L,OrderStatus.PAID);
		Order order3 = OrderFixture.createOrderWithOrderItems3(3L,OrderStatus.PAID);

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

		stockRepository.save(ProductStock.createInit(product1.getId(), 30));
		stockRepository.save(ProductStock.createInit(product2.getId(), 30));
		stockRepository.save(ProductStock.createInit(product3.getId(), 30));

		return List.of(product1,product2,product3);
	}


}