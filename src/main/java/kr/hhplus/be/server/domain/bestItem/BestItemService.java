package kr.hhplus.be.server.domain.bestItem;

import kr.hhplus.be.server.application.bestItem.BestItemsCacheDto;
import kr.hhplus.be.server.infrastructure.redis.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestItemService {

	private final BestItemRepository bestItemRepository;
	private final RedisRepository redisRepository;

	private static final DateTimeFormatter DAILY_FORMAT = DateTimeFormatter.BASIC_ISO_DATE;
	private static final DateTimeFormatter WEEKLY_FORMAT = DateTimeFormatter.ofPattern("YYYY-ww");

	@Transactional
	public BestItem save(BestItem bestItem) {return bestItemRepository.save(bestItem);}

//	@Cacheable(value = "bestItemCache", key = "'top10'")
	public List<BestItemsCacheDto> getTop10BestItems() {
		log.info("인기상품 캐시조회");
		List<BestItem> items = bestItemRepository.findTop10ByOrderBySalesCountDesc();
		return items.stream().map(BestItemsCacheDto::from).toList();
	}

	public Set<Object> getTop10LiveRank() {
		log.info("실시간 인기상품조회");
		String LIVE_RANK_KEY = "ranking:live";
		return redisRepository.getSoretedSetReverseRange(LIVE_RANK_KEY,10);
	}

	public Set<Object> getTop10DailyRank() {
		log.info("일간 인기상품 조회");
		String DAILY_KEY_PREFIX = "ranking:daily:";
		String dailyKey = DAILY_KEY_PREFIX + LocalDate.now().format(DAILY_FORMAT);
		return redisRepository.getSoretedSetReverseRange(dailyKey,10);
	}

	public Set<Object> getTop10WeeklyRank() {
		log.info("주간 인기상품 조회");
		String WEEK_KEY_PREFIX = "ranking:weekly:";
		String weekKey = WEEK_KEY_PREFIX + LocalDate.now().format(WEEKLY_FORMAT);
		return  redisRepository.getSoretedSetReverseRange(weekKey,10);
	}

	public BestItem findByProductId(Long id) {
		return bestItemRepository.findByProductId(id);
	}
}
