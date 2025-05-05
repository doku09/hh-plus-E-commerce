package kr.hhplus.be.server.common.lock.aop;

import kr.hhplus.be.server.common.lock.LockKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Order(Ordered.HIGHEST_PRECEDENCE)
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class DistrubutedLockAop {
	private final RedissonClient redissonClient;
	private final AopForTransaction aopForTransaction;

	@Around("@annotation(kr.hhplus.be.server.common.lock.aop.DistributedLockTransaction)")
	public Object lock(final ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();
		DistributedLockTransaction distributedLockTransaction = method.getAnnotation(DistributedLockTransaction.class);

		String key = LockKey.REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLockTransaction.key());
		RLock rLock = redissonClient.getLock(key);
		try {
			boolean available = rLock.tryLock(distributedLockTransaction.waitTime(), distributedLockTransaction.leaseTime(), distributedLockTransaction.timeUnit());
			if(!available){
				return false;
			}
			return aopForTransaction.proceed(joinPoint);
		} catch (InterruptedException e) {
			throw new InterruptedException();
		} finally {
			try {
				rLock.unlock();
			} catch (IllegalMonitorStateException e) {
				log.info("Redisson Lock Already UnLock {} {}", method.getName(),key);
			}
		}

	}
}
