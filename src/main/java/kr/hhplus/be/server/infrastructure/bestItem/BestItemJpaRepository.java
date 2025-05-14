package kr.hhplus.be.server.infrastructure.bestItem;

import kr.hhplus.be.server.domain.bestItem.BestItem;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


// TODO: 테스트
@Repository
public interface BestItemJpaRepository extends JpaRepository<BestItem, Long> {
	@Query(
		"SELECT b FROM BestItem b " +
			"WHERE b.createdAt >= :from " +
			"ORDER BY b.salesCount DESC "
	)
	List<BestItem> findBestItemsTopCount(@Param("from") LocalDateTime from, PageRequest pageRequest);

	BestItem findByProductId(Long id);

	List<BestItem> findTop10ByOrderBySalesCountDesc();
}
