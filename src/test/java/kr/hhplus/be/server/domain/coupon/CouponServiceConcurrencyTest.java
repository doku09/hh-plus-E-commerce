package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.OptimisticLockException;
import kr.hhplus.be.server.common.lock.LockKey;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.redis.RedisRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class CouponServiceConcurrencyTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponDistributedLockService couponDistributedLockService;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private ConcurrencyExecutor executor;
	@Autowired
	private UserCouponJpaRepository userCouponJpaRepository;
	@Autowired
	private CouponJpaRepository couponJpaRepository;
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private RedisRepository redisRepository;
	@Autowired
	private RedisCacheManager redisCacheManager;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	private LocalDateTime startDate = LocalDateTime.now().minusDays(10);
	private LocalDateTime endDate = LocalDateTime.now().plusDays(30);

	@AfterEach
	void tearDown() {
		userCouponJpaRepository.deleteAllInBatch();
		couponJpaRepository.deleteAllInBatch();
		userJpaRepository.deleteAllInBatch();

		redisCacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(redisCacheManager.getCache(cacheName)).clear());
		redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("*")));
	}

	@Test
	@DisplayName("[성공] 동일한 유저가 동시에 같은 쿠폰 발급 요청 시 비관적 락에 의해 쿠폰 하나만 발급한다. ")
	void concurrency_coupon_success () {

		// given
		User savedUser = userRepository.save(User.create("테스터"));

		Coupon coupon = couponRepository.saveCoupon(Coupon.create(
			"선착순 쿠폰", 1000L, 5,CouponType.LIMITED,
			LocalDateTime.now(), LocalDateTime.now().plusDays(7)
		));

		long couponId = coupon.getId();

		// when
		UserCouponCommand.Issue issueCommand = UserCouponCommand.Issue.of(couponId, savedUser.getId());

		AtomicInteger successCount = new AtomicInteger();
		executor.execute(() -> {
			try {
				couponService.issueCouponWithPessimisticLock(issueCommand);
				successCount.incrementAndGet();
			} catch (OptimisticLockException e) {
				e.printStackTrace();
			}
		},3);


		// then
		List<UserCoupon> issued = couponRepository.findAllUserCoupon();

		assertThat(successCount.get()).isEqualTo(1); // 성공한 요청 수
		assertThat(issued).hasSize(1); // 발급된 UserCoupon 수
	}

	
	@Test
	@DisplayName("[성공] 선착순 쿠폰이 있을때 여러 사용자가 동시에 접근해도 비관적 락을 사용하여 정상적으로 발급한다.")
	void issue_coupon_concurrency_pessimistic_success() throws InterruptedException {

		// given
		for (int i = 0; i < 10; i++) {
			userRepository.save(User.create("테스터"));
		}

		List<User> users = userRepository.findAll();
		Coupon coupon = createTenCoupon();

		long couponId = coupon.getId();

		// when
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(10);
		AtomicInteger successCount = new AtomicInteger();
		for (int i = 0; i < 10; i++) {
			UserCouponCommand.Issue issueCommand = UserCouponCommand.Issue.of(couponId, users.get(i).getId());

			executorService.submit(() -> {
				try {
					couponService.issueCouponWithPessimisticLock(issueCommand);
					successCount.incrementAndGet();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();
		executorService.shutdown();
		// then
		List<UserCoupon> issued = couponRepository.findAllUserCoupon();
		Coupon findCoupon = couponRepository.findCouponById(couponId).orElse(null);

		assertThat(successCount.get()).isEqualTo(10);
		assertThat(issued.size()).isEqualTo(successCount.get());
		assertThat(findCoupon.getQuantity()).isEqualTo(10 - successCount.get());
	}

	@Test
	@DisplayName("[성공] simpleLock 적용하여 10명의 유저에게 10개의 쿠폰을 정상적으로 발급한다.")
	void simpleLock_issue_coupon() throws InterruptedException {
		// given
		int threadCount = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(10);

		// 유저 10명 생성
		List<Long> userIds = createUsers(10);
		// 10장 생성
		Coupon coupon = createTenCoupon();

		// when
		for (int i = 0; i < threadCount; i++) {
			Long userId = userIds.get(i);
			UserCouponCommand.Issue issue = UserCouponCommand.Issue.of(coupon.getId(), userId);

			executorService.submit(() -> {
					try {
						couponDistributedLockService.issueCouponWithSimpleLock(issue);
					} catch (Exception e) {
						System.out.println(e.getClass().getSimpleName());
					} finally {
						latch.countDown();
					}
			});
		}
		latch.await();
		executorService.shutdown();

	  // then
		Coupon result = couponRepository.findCouponById(coupon.getId()).orElse(null);

		assertThat(result).isNotNull();
		assertThat(result.getQuantity()).isEqualTo(9);
	}


	@Test
	@DisplayName("[성공] Aop로 분산락 적용하여 10명의 유저에게 10개의 쿠폰을 정상적으로 발급한다.")
	void AopLock_issue_coupon() throws InterruptedException {

		// given
		List<Long> userIds = createUsers(10);
		Coupon coupon = createTenCoupon();

		int threadCount = 10;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			Long userId = userIds.get(i);
			executorService.submit(() -> {
				try{
					UserCouponCommand.Issue issue = UserCouponCommand.Issue.of(coupon.getId(), userId);
					couponService.issueCouponWithAnnoationLock(LockKey.COUPON,issue);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();
		// then
		Coupon result = couponRepository.findCouponById(coupon.getId()).orElse(null);

		assertThat(result).isNotNull();
		assertThat(result.getQuantity()).isEqualTo(0);
	}

	private List<Long> createUsers(int num) {
		List<Long> userIds = new ArrayList<>();

		for (int i = 1; i <= num; i++) {
			User user = userRepository.save(User.create("테스터" + i));
			userIds.add(user.getId());
		}

		return userIds;
	}

	private Coupon createTenCoupon() {
		Coupon coupon = couponRepository.saveCoupon(CouponFixture.create(10));
		return coupon;
	}

	@Test
	@DisplayName("캐시에 있는 쿠폰을 동시에 발급 요청시 동시성 이슈가 발생한다.")
	void issue_coupon_with_redis_duplicate() throws InterruptedException {

		// given
		int threadCount = 15;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);

		List<Long> userIds = createUsers(15);
		CouponCommand.Create command = CouponCommand.Create.of(
			"깜짝쿠폰",
			1000L,
			10,
			CouponType.LIMITED,
			startDate,
			endDate
		);

		CouponInfo.Coupon savedCoupon = couponService.register(command);
		couponService.loadFcFsCoupon(savedCoupon.getId());

		// when
		for (int i = 0; i < threadCount; i++) {
			Long userId = userIds.get(i);
			executorService.submit(() -> {
				try{
					startLatch.await();
					UserCouponCommand.Issue issue = UserCouponCommand.Issue.of(savedCoupon.getId(), userId);
					couponService.issueCouponWithRedisCounter(issue);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					latch.countDown();
				}
			});
		}

		startLatch.countDown();
		latch.await();
		executorService.shutdown();

		// then
		String couponKey = "coupon:" + savedCoupon.getId() + ":remaining";
		String count = redisRepository.sGet(couponKey);
		System.out.println("쿠폰남은 개수: " + count);
		assertThat(count).isLessThan(String.valueOf(0));
	}

	@Test
	@DisplayName("lua script를 사용하여 동시성을 보장한다.")
	void issue_coupon_with_lua() throws InterruptedException {

		// given
		int threadCount = 15;
		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		CountDownLatch latch = new CountDownLatch(threadCount);
		CountDownLatch startLatch = new CountDownLatch(1);

		List<Long> userIds = createUsers(15);
		CouponCommand.Create command = CouponCommand.Create.of(
			"깜짝쿠폰",
			1000L,
			10,
			CouponType.LIMITED,
			startDate,
			endDate
		);

		CouponInfo.Coupon savedCoupon = couponService.register(command);
		couponService.loadFcFsCoupon(savedCoupon.getId());

		// when
		for (int i = 0; i < threadCount; i++) {
			Long userId = userIds.get(i);
			executorService.submit(() -> {
				try{
					UserCouponCommand.Issue issue = UserCouponCommand.Issue.of(savedCoupon.getId(), userId);
					startLatch.await();
					couponService.issueCouponAtomic(issue);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					latch.countDown();
				}
			});
		}

		startLatch.countDown();
		latch.await();
		executorService.shutdown();

		// then
		String couponKey = "coupon:" + savedCoupon.getId() + ":remaining";
		String count = redisRepository.sGet(couponKey);
		assertThat(count).isEqualTo(String.valueOf(0));
	}
}
