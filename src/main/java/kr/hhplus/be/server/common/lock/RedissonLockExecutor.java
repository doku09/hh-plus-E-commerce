package kr.hhplus.be.server.common.lock;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * AOP 아닌 방식
 */
@RequiredArgsConstructor
@Component
@Slf4j
public class RedissonLockExecutor implements DistributedLockExecutor {

	private final RedissonClient redissonClient;

	@Override
	public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit, Supplier<T> supplier) {

		RLock lock = redissonClient.getLock(lockKey);

		boolean isLocked = false;
		log.info("redis 키 개수: {}",redissonClient.getKeys().count());
		try {
			isLocked = lock.tryLock(waitTime, leaseTime, timeUnit);
			log.info("isLocked: {}",isLocked);
			if (!isLocked) {
				throw new GlobalBusinessException(ErrorCode.FAILED_TO_ACQUIRE_LOCK);
			}
			return supplier.get();
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new GlobalBusinessException(ErrorCode.FAILED_TO_ACQUIRE_LOCK);
		} finally {
				log.info("unLocked: {}",isLocked);
			if (isLocked && lock.isHeldByCurrentThread()) {
				lock.unlock();
			}
		}
	}
}
