package kr.hhplus.be.server.application.bestItem;

import kr.hhplus.be.server.domain.bestItem.BestItem;
import kr.hhplus.be.server.domain.bestItem.BestItemService;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.infrastructure.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
	private final RedisRepository redisRepository;

	private static final DateTimeFormatter DAILY_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
	private static final DateTimeFormatter WEEKLY_FORMAT = DateTimeFormatter.ofPattern("YYYY-ww");
	public static final String DAILY_KEY_PREFIX = "ranking:daily:";
	public static final String WEEK_KEY_PREFIX = "ranking:weekly:";

	// 초(0-59) 분(0-59) 시(0-23) 일(1-31) 월(1-12) 요일(0-7, 일=0/7)
	// 0 0 * * * *   > 한 시간마다
	// 0 */5 * * * * > 매 5분
	// 0 0 0 * * *   > 매일 자정
	@Scheduled(cron = "0 50 23 * * *")
	@Transactional
	public void warmDailyRanking() {
		List<OrderItem> OrderOneDayStats = orderService.getOrderBeforeHour(24);

		Map<Long, Long> salesOneDayMap = getSalesMap(OrderOneDayStats);

		salesOneDayMap.forEach((productId, salesQuantity) -> {
			Product product = productService.findById(productId);
			String dailyKey = DAILY_KEY_PREFIX + LocalDate.now().format(DAILY_FORMAT);
			redisRepository.addSortedSetWithTTL(dailyKey,String.valueOf(product.getId()),salesQuantity,Duration.ofHours(25));
		});
	}

	@Scheduled(cron = "0 50 23 * * *")
	@Transactional
	public void warmWeeklyRanking() {
		List<OrderItem> orderOneWeekStats = orderService.getOrderBeforeDay(7);

		Map<Long, Long> salesOneWeekMap = getSalesMap(orderOneWeekStats);

		salesOneWeekMap.forEach((productId, salesQuantity) -> {
			Product product = productService.findById(productId);
			String weekKey = WEEK_KEY_PREFIX + LocalDate.now().format(WEEKLY_FORMAT);
			redisRepository.addSortedSetWithTTL(weekKey,String.valueOf(product.getId()),salesQuantity,Duration.ofHours(25));
		});
	}


	private static Map<Long, Long> getSalesMap(List<OrderItem> OrderOneDayStats) {
		return OrderOneDayStats.stream().collect(Collectors.groupingBy(
			OrderItem::getProductId,
			Collectors.summingLong(OrderItem::getQuantity)
		));
	}

}
