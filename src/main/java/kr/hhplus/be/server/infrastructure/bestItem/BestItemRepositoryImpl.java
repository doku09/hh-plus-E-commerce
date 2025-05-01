package kr.hhplus.be.server.infrastructure.bestItem;

import kr.hhplus.be.server.domain.bestItem.BestItem;
import kr.hhplus.be.server.domain.bestItem.BestItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BestItemRepositoryImpl implements BestItemRepository {

	private final BestItemJpaRepository bestItemJpaRepository;

	@Override
	public List<BestItem> findBestItemsTopCount(LocalDateTime from, int limit) {
		return bestItemJpaRepository.findBestItemsTopCount(from, PageRequest.of(0, limit));
	}

	@Override
	public BestItem save(BestItem bestItem) {
		return bestItemJpaRepository.save(bestItem);
	}

	@Override
	public BestItem findByProductId(Long id) {
		return bestItemJpaRepository.findByProductId(id);
	}
}
