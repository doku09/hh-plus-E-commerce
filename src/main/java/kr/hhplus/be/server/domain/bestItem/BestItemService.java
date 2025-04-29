package kr.hhplus.be.server.domain.bestItem;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BestItemService {

	private final BestItemRepository bestItemRepository;

	@Transactional
	public BestItem save(BestItem bestItem) {return bestItemRepository.save(bestItem);}

	public List<BestItem> getBestItems(LocalDateTime now, int days, int limit) {
		LocalDateTime from = now.minusDays(days);

		return bestItemRepository.findBestItemsTopCount(from, limit);
	}

	public BestItem findByProductId(Long id) {
		return bestItemRepository.findByProductId(id);
	}
}
