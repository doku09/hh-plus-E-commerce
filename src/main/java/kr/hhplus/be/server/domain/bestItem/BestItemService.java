package kr.hhplus.be.server.domain.bestItem;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BestItemService {

	private final BestItemRepository bestItemRepository;

	@Transactional
	public BestItem save(BestItem bestItem) {return bestItemRepository.save(bestItem);}

	@Cacheable(value = "bestItems", key = "'top10'")
	@Transactional
	public List<BestItem> getTop10BestItems() {

		return bestItemRepository.findTop10ByOrderBySalesCountDesc();
	}

	public BestItem findByProductId(Long id) {
		return bestItemRepository.findByProductId(id);
	}
}
