package kr.hhplus.be.server.domain.bestItem;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;


public interface BestItemRepository {
	List<BestItem> findBestItemsTopCount(LocalDateTime from, int limit);

	BestItem save(BestItem bestItem);

	BestItem findByProductId(Long id);

	List<BestItem> findTop10ByOrderBySalesCountDesc();

	void incrSortedSet(String key, String value, double quantity, Duration ttl);
}
