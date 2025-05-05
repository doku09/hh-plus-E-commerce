package kr.hhplus.be.server.domain.point;

import jakarta.persistence.OptimisticLockException;
import kr.hhplus.be.server.common.exception.NotFoundUserException;
import kr.hhplus.be.server.common.lock.aop.DistributedLockTransaction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.redisson.api.RedissonClient;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final RedissonClient redissonClient;

	/**
	 * 사용자에게 포인트를 충전합니다.
	 */
	@Transactional
	public PointInfo.Point charge(PointCommand.Charge command) {

		Point point = pointRepository.findByUserId(command.getUserId()).orElseGet(
			() -> pointRepository.save(Point.of(Point.ZERO_POINT,command.getUserId())));

		point.charge(command.getAmount());

		return PointInfo.Point.of(point.getAmount());
	}

	/**
	 * 사용자 포인트를 사용합니다.
	 */
	@Retryable(retryFor = {
		OptimisticLockException.class,
		StaleObjectStateException.class,
		ObjectOptimisticLockingFailureException.class
	}, maxAttempts = 5, backoff = @Backoff(delay = 100))
	@Transactional
	public PointInfo.Point use(PointCommand.Use command) {
			Point point = pointRepository.findByUserId(command.getUserId()).orElseGet(
				() -> pointRepository.save(
					Point.of(Point.ZERO_POINT, command.getUserId())
				)
			);

			point.use(command.getAmount());
			return PointInfo.Point.of(point.getAmount());
	}

	@DistributedLockTransaction(key = "#lockName.concat(':').concat(#command.getUserId())")
	public PointInfo.Point useWithAOPLock(String lockName,PointCommand.Use command) {
			Point point = pointRepository.findByUserId(command.getUserId()).orElseGet(
				() -> pointRepository.save(
					Point.of(Point.ZERO_POINT, command.getUserId())
				)
			);

			point.use(command.getAmount());
			return PointInfo.Point.of(point.getAmount());
	}

	/**
	 * 사용자의 현재 포인트를 조회합니다.
	 */
	public Point get(Long userId) {
		return pointRepository.findByUserId(userId)
			.orElseThrow(NotFoundUserException::new);
	}
}
