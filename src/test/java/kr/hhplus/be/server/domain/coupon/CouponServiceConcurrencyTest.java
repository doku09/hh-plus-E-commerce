package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.OptimisticLockException;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.time.LocalDateTime;
import java.util.List;
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
	private CouponRepository couponRepository;
	@Autowired
	private ConcurrencyExecutor executor;

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
				couponService.issueCoupon(issueCommand);
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

		Coupon coupon = couponRepository.saveCoupon(Coupon.create(
			"선착순 쿠폰", 1000L, 10,CouponType.LIMITED,
			LocalDateTime.now(), LocalDateTime.now().plusDays(7)
		));

		long couponId = coupon.getId();

		// when
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		CountDownLatch latch = new CountDownLatch(10);
		AtomicInteger successCount = new AtomicInteger();
		for (int i = 0; i < 10; i++) {
			UserCouponCommand.Issue issueCommand = UserCouponCommand.Issue.of(couponId, users.get(i).getId());
			executorService.submit(() -> {
				try {
					couponService.issueCoupon(issueCommand);
					successCount.incrementAndGet();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					latch.countDown();
				}
			});
		}
		latch.await();

		// then
		List<UserCoupon> issued = couponRepository.findAllUserCoupon();
		Coupon findCoupon = couponRepository.findCouponById(couponId).orElse(null);

		assertThat(successCount.get()).isEqualTo(10);
		assertThat(issued.size()).isEqualTo(successCount.get());
		assertThat(findCoupon.getQuantity()).isEqualTo(10 - successCount.get());
	}
}
