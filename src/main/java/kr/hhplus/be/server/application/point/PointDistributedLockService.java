package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointInfo;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Slf4j
@RequiredArgsConstructor
@Component
public class PointDistributedLockService {

	private final PointService pointService;
	private final RedissonClient redissonClient;

	public void useWithSimpleLock(PointCommand.Use command) {
		log.info("[{}]" + " 포인트 사용 요청",Thread.currentThread().getName());
		RLock lock = redissonClient.getLock("lock:point:" + command.getUserId());
		boolean isLocked = false;

		try {
			log.info("before Lock: {}",isLocked);
			isLocked = lock.tryLock(0, 0, TimeUnit.SECONDS);
			log.info("after Lock: {}",isLocked);
			if(!isLocked) {
				log.info("포인트 사용 Lock 획득 실패");
				throw new IllegalStateException("포인트 사용 Lock 획득 실패");
			}

			PointInfo.Point point = pointService.use(command);
		} catch (InterruptedException e) {
			throw new RuntimeException("포인트 사용 Lock 획득 실패", e);
		} finally {
			if(isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
