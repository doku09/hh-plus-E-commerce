package kr.hhplus.be.server.application.bestItem;

import kr.hhplus.be.server.domain.bestItem.BestItem;
import kr.hhplus.be.server.domain.bestItem.BestItemService;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional
@RequiredArgsConstructor
@Component
public class BestItemScheduler {

	private final OrderService orderService;
	private final ProductService productService;
	private final BestItemService bestItemService;
	private final RedisTemplate<String, Object> redisTemplate;

	// 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7, 일=0/7)
	// 0 0 * * * *   > 한 시간마다
	// 0 */5 * * * * > 매 5분
	// 0 0 0 * * *   > 매일 자정

	@Scheduled(cron = "0 0 * * * *")
	@Transactional
	public void saveBestItem() {
		log.info("인기상품 저장");
		List<OrderItem> orderItems = orderService.getOrderBeforeHour(1);

		Map<Long, Long> salesMap = orderItems.stream().collect(Collectors.groupingBy(
			OrderItem::getProductId,
			Collectors.summingLong(OrderItem::getQuantity)
		));

		salesMap.forEach((productId, salesQuantity) -> {
			// Product 정보를 조회합니다.
			Product product = productService.findById(productId);
			if (product == null) {
				throw new IllegalArgumentException("Product not found for ID: " + productId);
			}

			// BestItem 객체를 생성합니다.
			BestItem findItem = bestItemService.findByProductId(product.getId());

			if(null == findItem) {
				findItem = bestItemService.save(BestItem.create(product, 0L));
			}

			findItem.updateSalesCount(salesQuantity);
		});
	}

	@Scheduled(cron = "0 */50 * * * *")
	public void cacheBestItems() {
		log.info("인기상품 탑10 캐시적재");

		// 1. DB에서 인기 상품 조회
		List<BestItemsCacheDto> top10BestItems = bestItemService.getTop10BestItems();

		// 2. Redis에 저장 (TTL: 15초)
		try {
			redisTemplate.opsForValue().set("bestItems:top10", top10BestItems, Duration.ofSeconds(15));
		} catch (Exception e) {
			log.error("캐시 적재 실패", e);
		}
	}

}
