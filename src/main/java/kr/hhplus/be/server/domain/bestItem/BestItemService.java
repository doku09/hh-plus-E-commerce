package kr.hhplus.be.server.domain.bestItem;

import kr.hhplus.be.server.application.bestItem.BestItemsCacheDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BestItemService {

	private final BestItemRepository bestItemRepository;

	@Transactional
	public BestItem save(BestItem bestItem) {return bestItemRepository.save(bestItem);}

	@Cacheable(value = "bestItems", key = "'top10'")
	@Transactional
	public List<BestItemsCacheDto> getTop10BestItems() {
		log.info("인기상품 캐시조회");
		List<BestItem> items = bestItemRepository.findTop10ByOrderBySalesCountDesc();

		return items.stream().map(BestItemsCacheDto::from).toList();
	}

	public BestItem findByProductId(Long id) {
		return bestItemRepository.findByProductId(id);
	}
}
