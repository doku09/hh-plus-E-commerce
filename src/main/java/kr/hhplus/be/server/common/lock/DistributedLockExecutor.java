package kr.hhplus.be.server.common.lock;

import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * lockKey: 분산 락의 키
 * waitTime: 락을 기다릴 최대 시간
 * leaseTime: 락이 자동으로 해제될 시간
 * supplier: 실제 비즈니스 로직
 */
public interface DistributedLockExecutor {
	<T> T executeWithLock(String lockKey, long waitTime, long leaseTime, TimeUnit timeUnit, Supplier<T> supplier);
}
