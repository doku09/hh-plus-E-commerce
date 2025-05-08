## 왜 AOP인가

### 왜 락 획득 후 트랜잭션을 시작해야할까?
- 기본적으로 락은 트랜잭션이 시작되기 이전에 획득하고 트랜잭션이 종료된후 해제되어야 한다.
- 트랜잭션이 시작되어 데이터를 조회한 이후 락을 획득하면 동시에 처리되는 앞단의 트랜잭션 커밋 결과를 확인하지 못한채 데이터가 처리되므로 동시성 이슈가 발생한다. 
- 또 트랜잭션 시작된 이후 락 획득에 실패하면 의미없는 트랜잭션이 발생한다.


### 왜 AOP를 사용해서 분산락을 구현해야할까?
- AOP를 몰랐던 나는 쿠폰발급 로직에 분산락을 구현하기 위해 CouponLockService를 만들고 락을 시작한뒤 트랜잭션을 시작하도록 내부에서 비즈니스로직을 실행하도록 하였다.  
```java
 class couponLockService {
	
    @Autowired
    private final RedissonClient redissonClient;
    @Autowired
    private final CouponService couponService;

	public Coupon issueCouponWithSimpleLock(UserCouponCommand.Issue command) {

		String lockKey = LockKey.COUPON + command.getCouponId();

		// waitTime을 0으로 하면 기다리지 않고 포기하여 simpleLock과 같다
		return lockExecutor.executeWithLock(lockKey,0,5, TimeUnit.SECONDS,
			() -> couponService.issueCoupon(command));
	}
}
```
이렇게 했을때 문제점은 
- 비즈니스가 있는 couponService가 있음에도, 락을 위한 
- 클래스(couponLockService.class)가 필요하며 레이어를 하나 더 만들어야 하는 문제가 발생하였다.
- couponService를 사용하는 facade를 만들때도 facade를 래핑한 클래스를 만들어야한다.
- 락의 적용여부에 따라 클래스가 달라지므로 컨트롤러에서 호출하는 코드도 상황에 따라 변한다.

이러한 문제로 인해 AOP를 적용하기로 하였다.

```java
@Component
public class AopForTransaction {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
```
 
   