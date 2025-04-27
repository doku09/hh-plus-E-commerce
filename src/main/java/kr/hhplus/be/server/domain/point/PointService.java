package kr.hhplus.be.server.domain.point;

import jakarta.persistence.OptimisticLockException;
import kr.hhplus.be.server.common.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.StaleObjectStateException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

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
		log.info("[{}]" + " 포인트 사용 요청",Thread.currentThread().getName());
		RLock lock = redissonClient.getLock("lock:point:" + command.getUserId());
		boolean isLocked = false;

		try {
			isLocked = lock.tryLock(1, 5, TimeUnit.SECONDS);
			log.info("isLocked: {}",isLocked);
			if(!isLocked) {
				log.info("포인트 사용 Lock 획득 실패");
				throw new IllegalStateException("포인트 사용 Lock 획득 실패");
			}

			Point point = pointRepository.findByUserId(command.getUserId()).orElseGet(
				() -> pointRepository.save(
					Point.of(Point.ZERO_POINT, command.getUserId())
				)
			);

			point.use(command.getAmount());
			return PointInfo.Point.of(point.getAmount());

		} catch (InterruptedException e) {
			throw new RuntimeException("포인트 사용 Lock 획득 실패", e);
		} finally {
			if(isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}

	/**
	 * 사용자의 현재 포인트를 조회합니다.
	 */
	public Point get(Long userId) {
		return pointRepository.findByUserId(userId)
			.orElseThrow(NotFoundUserException::new);
	}
}
