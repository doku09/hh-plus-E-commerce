package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@ActiveProfiles("test")
@SpringBootTest
class OrderFacadeConcurrencyTest {

	@Autowired
	private OrderFacade orderFacade;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private ProductStockRepository productStockRepository;
	@Autowired
	private PointRepository pointRepository;
	@Autowired
	private ConcurrencyExecutor concurrencyExecutor;

	@Test
	@DisplayName("[성공] 주문요청이 동시에 들어와도 순차적으로 처리되어야 한다.")
	void concurrency_order_success() {

	  // given

		// 사용자 생성
		User user = User.create("테스터");
		userRepository.save(user);

		// 포인트 생성
		Point point = Point.of(50_000L, user.getId());
		pointRepository.save(point);

		// 상품 생성
		Product product = Product.create("스테이크", 10_000L);
		productRepository.save(product);

		// 상품 재고 생성
		ProductStock stock = ProductStock.createInit(product.getId(), 5);
		productStockRepository.save(stock);

		// 상품 1개를 쿠폰없이 주문
		OrderCriteria.OrderItem orderItem = OrderCriteria.OrderItem.of(product.getId(), 1);
		OrderCriteria.CreateOrder orderCriteria = OrderCriteria.CreateOrder.of(user.getId(),null, List.of(orderItem));

		//동시성 테스트를 위한 설정
		int threadCount = 5;
		List<String> results = Collections.synchronizedList(new ArrayList<>());

		concurrencyExecutor.execute(() -> {
		try {
				orderFacade.order(orderCriteria);
				results.add("success");
			} catch (Exception e) {
			results.add(e.getClass().getSimpleName());
			throw e;
		}
		},threadCount);

	  // then
		long successCount = results.stream().filter(r -> r.equals("success")).count();
		long failureCount = threadCount - successCount;

		System.out.println("성공 횟수: " + successCount);
		System.out.println("실패 횟수: " + failureCount);
		System.out.println("예외 목록: " + results);


		assertThat(successCount).isEqualTo(5);
	}


	@Test
	@DisplayName("[성공] 주문을 순차대로하면 정상적으로 재고차감이 이루어진다.")
	void sequence_order_success() throws InterruptedException {

		// given
		// 사용자 생성
		User user = User.create("테스터");
		userRepository.save(user);

		// 포인트 생성
		Point point = Point.of(50_000L, user.getId());
		pointRepository.save(point);

		// 상품 생성
		Product product = Product.create("스테이크", 10_000L);
		productRepository.save(product);

		// 상품 재고 생성
		ProductStock stock = ProductStock.createInit(product.getId(), 5);
		productStockRepository.save(stock);

		// 상품 1개를 쿠폰없이 주문
		OrderCriteria.OrderItem orderItem = OrderCriteria.OrderItem.of(product.getId(), 1);
		OrderCriteria.CreateOrder orderCriteria = OrderCriteria.CreateOrder.of(user.getId(),null, List.of(orderItem));

		orderFacade.order(orderCriteria);
		orderFacade.order(orderCriteria);
		orderFacade.order(orderCriteria);

		ProductStock result = productStockRepository.findByProductId(product.getId());

		// then
		assertThat(result.getQuantity()).isEqualTo(2);
	}

	@Test
	@DisplayName("[성공] AOP 분산락으로 동시 주문에 대한 요청을 처리한다.")
	void aop_concurrency_order() {
		// given

		// 사용자 생성
		User user = User.create("테스터");
		userRepository.save(user);

		// 포인트 생성
		Point point = Point.of(50_000L, user.getId());
		pointRepository.save(point);

		// 상품 생성
		Product product = Product.create("스테이크", 10_000L);
		productRepository.save(product);

		// 상품 재고 생성
		ProductStock stock = ProductStock.createInit(product.getId(), 5);
		productStockRepository.save(stock);

		// 상품 1개를 쿠폰없이 주문
		OrderCriteria.OrderItem orderItem = OrderCriteria.OrderItem.of(product.getId(), 1);
		OrderCriteria.CreateOrder orderCriteria = OrderCriteria.CreateOrder.of(user.getId(),null, List.of(orderItem));

		//동시성 테스트를 위한 설정
		int threadCount = 5;
		List<String> results = Collections.synchronizedList(new ArrayList<>());

		concurrencyExecutor.execute(() -> {
			try {
				orderFacade.orderWithAopDistributedLock("order",orderCriteria);
				results.add("success");
			} catch (Exception e) {
				results.add(e.getClass().getSimpleName());
				throw e;
			}
		},threadCount);

		// then
		long successCount = results.stream().filter(r -> r.equals("success")).count();
		long failureCount = threadCount - successCount;

		System.out.println("성공 횟수: " + successCount);
		System.out.println("실패 횟수: " + failureCount);
		System.out.println("예외 목록: " + results);


		assertThat(successCount).isEqualTo(5);
		assertThat(failureCount).isEqualTo(0);
	}
}