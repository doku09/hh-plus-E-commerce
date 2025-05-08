package kr.hhplus.be.server.application.order;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
//@Component
@RequiredArgsConstructor
public class OrderScheduler {

	private final OrderFacade orderFacade;
	private final Random random = new Random();
	@Scheduled(cron = "*/10 * * * * *") // 10초마다 실행
	public void createRandomOrder() {
		long userId = getRandomUserId();
		Long couponId = maybeGetCouponId();
		List<OrderCriteria.OrderItem> items = generateRandomOrderItems();

		OrderCriteria.CreateOrder order = OrderCriteria.CreateOrder.of(userId, couponId, items);
			log.info("=================");
		for (OrderCriteria.OrderItem orderItem : order.getOrderItems()) {
			log.info("주문상품: orderItem:{}",orderItem.getProductId());
		}
			log.info("=================");
		// 주문 처리 서비스 호출 예시
		OrderResult.Order ordered = orderFacade.order(order);
	}

	private long getRandomUserId() {
		return random.nextInt(10) + 1; // 1~100번 사용자
	}

	private Long maybeGetCouponId() {
		return null;
	}

	private List<OrderCriteria.OrderItem> generateRandomOrderItems() {
		int itemCount = random.nextInt(3) + 1; // 1~3개 상품 주문
		List<OrderCriteria.OrderItem> items = new ArrayList<>();
		for (int i = 0; i < itemCount; i++) {
			long productId = random.nextInt(10) + 1; // 1~10번 상품
			int quantity = random.nextInt(5) + 1; // 수량 1~5
			items.add(OrderCriteria.OrderItem.of(productId, quantity));
		}
		return items;
	}

	@PostConstruct
	private void init() {
		log.info("주문 초기화");
	}
}
