package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.common.lock.DistributedLockExecutor;
import kr.hhplus.be.server.common.lock.LockKey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CouponDistributedLockService {

	private final CouponService couponService;
	private final DistributedLockExecutor lockExecutor;


	public Coupon issueCouponWithSimpleLock(UserCouponCommand.Issue command) {

		String lockKey = LockKey.COUPON + command.getCouponId();

		// waitTime을 0으로 하면 기다리지 않고 포기하여 simpleLock과 같다
		return lockExecutor.executeWithLock(lockKey,0,5, TimeUnit.SECONDS,
			() -> couponService.issueCoupon(command));
	}

	public Coupon issueCouponWithPubSubLock(UserCouponCommand.Issue command) {

		String lockKey = LockKey.COUPON + command.getCouponId();

		return lockExecutor.executeWithLock(lockKey,1,5, TimeUnit.SECONDS,
			() -> couponService.issueCoupon(command));
	}
}
