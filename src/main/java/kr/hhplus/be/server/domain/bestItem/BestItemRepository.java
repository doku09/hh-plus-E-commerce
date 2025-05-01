package kr.hhplus.be.server.domain.bestItem;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


public interface BestItemRepository {
	List<BestItem> findBestItemsTopCount(LocalDateTime from, int limit);
	BestItem save(BestItem bestItem);

	BestItem findByProductId(Long id);
}
