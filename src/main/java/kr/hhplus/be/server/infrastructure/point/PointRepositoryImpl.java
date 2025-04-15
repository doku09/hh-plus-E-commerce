package kr.hhplus.be.server.infrastructure.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {

	private final PointJpaRepository pointJpaRepository;

	@Override
	public Optional<Point> findByUserId(Long userId) {
		return Optional.empty();
	}

	@Override
	public Point save(Point point) {
		return null;
	}

	@Override
	public Point update(Point point) {
		return null;
	}
}
