package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.common.exception.NotEnoughPointException;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

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
	private PointRepository pointRepository;
	@Autowired
	private ConcurrencyExecutor concurrencyExecutor;

	@Test
	@DisplayName("[실패] 주문요청이 동시에 들어와도 순차적으로 처리되어야 한다.")
	void concurrency_order_success() throws InterruptedException {

	  // given

		// 사용자 생성
		User user = User.create("테스터");
		userRepository.save(user);

		// 포인트 생성
		Point point = Point.of(500_000L, user.getId());
		pointRepository.save(point);

		// 상품 생성
		Product product = Product.create("스테이크", 10_000L);
		productRepository.save(product);

		// 상품 재고 생성
		ProductStock stock = ProductStock.createInit(product, 15);
		productRepository.saveStock(stock);

		// 상품 1개를 쿠폰없이 주문
		OrderCriteria.OrderItem orderItem = OrderCriteria.OrderItem.of(product.getId(), 1);
		OrderCriteria.CreateOrder orderCriteria = OrderCriteria.CreateOrder.of(user.getId(),null, List.of(orderItem));

		//동시성 테스트를 위한 설정
		int threadCount = 10;

		concurrencyExecutor.execute(() -> orderFacade.order(orderCriteria),threadCount);

	  // then
		ProductStock resultStock = productRepository.findStockByProductId(product.getId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_ENOUGH_STOCK));
		assertThat(resultStock.getQuantity()).isEqualTo(5);

		Point resultPoint = pointRepository.findByUserId(user.getId()).orElseThrow(NotEnoughPointException::new);
		assertThat(resultPoint.getAmount()).isEqualTo(0L);
	}

}