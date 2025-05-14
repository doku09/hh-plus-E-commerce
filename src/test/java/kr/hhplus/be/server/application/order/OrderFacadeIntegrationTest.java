package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.RankingUpdater;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.redis.RedisRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@ActiveProfiles("test")
@SpringBootTest
class OrderFacadeIntegrationTest {

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
	private RedisRepository redisRepository;

	@Test
	@DisplayName("[성공] 주문요청시 실시간 인기상품 집계를 위해 데이터가 저장된다.")
	void concurrency_order_success() {

		// given
		// 사용자 생성
		User user = User.create("테스터");
		userRepository.save(user);

		// 포인트 생성
		Point point = Point.of(1_000_000L, user.getId());
		pointRepository.save(point);

		// 상품 생성
		Product product1 = Product.create("스테이크", 10_000L);
		Product product2 = Product.create("수박", 10_000L);
		productRepository.save(product1);
		productRepository.save(product2);

		// 상품 재고 생성
		ProductStock stock = ProductStock.createInit(product1.getId(), 30);
		ProductStock stock2 = ProductStock.createInit(product2.getId(), 30);
		productStockRepository.save(stock);
		productStockRepository.save(stock2);

		// 상품 1개를 쿠폰없이 주문
		OrderCriteria.OrderItem orderItem1 = OrderCriteria.OrderItem.of(product1.getId(), 10);
		OrderCriteria.OrderItem orderItem2 = OrderCriteria.OrderItem.of(product2.getId(), 20);
		OrderCriteria.CreateOrder orderCriteria = OrderCriteria.CreateOrder.of(user.getId(),null, List.of(orderItem1,orderItem2));

		// when
		OrderResult.Order ordered = orderFacade.order(orderCriteria);

		// then
		String LIVE_RANK_KEY = "ranking:live";
		Set<Object> result = redisRepository.reverseRange(LIVE_RANK_KEY, 0L, 10L);

		assertThat(result).extracting(o -> ((String)o)).containsExactly(String.valueOf(product2.getId()),String.valueOf(product1.getId()));
	}

}