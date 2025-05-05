package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.application.point.PointDistributedLockService;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PointServiceConcurrencyTest {

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConcurrencyExecutor executor;

	@Autowired
	private PointService pointService;


	@Test
	@Order(1)
	@DisplayName("[성공] 동시에 포인트를 사용하면 재시도하여 성공한다.")
	void concurrency_use_point() {

		// given
		int threadCount = 5;

		User saveduser = userRepository.save(User.create("테스터"));
		pointRepository.save(Point.of(100_000L,saveduser.getId()));

		PointCommand.Use command = PointCommand.Use.of(saveduser.getId(), 5000L);

		// when
		AtomicInteger successCount = new AtomicInteger();

		executor.execute(() -> {
			try {
				pointService.use(command);
				successCount.incrementAndGet();
			} catch (ObjectOptimisticLockingFailureException e) {
				System.out.println("충돌 발생: " + e.getMessage());
			}
		}, threadCount);

		// then
		Point point = pointRepository.findByUserId(saveduser.getId()).orElse(null);

		assertThat(point).isNotNull();
		assertThat(point.getAmount()).isEqualTo(75000L);
	}

	@Test
	@Order(2)
	@DisplayName("[성공] AOP분산락 사용하 여포인트 차감시 동시성문제를 해결한다.")
	void aop_distributed_use_point() {
		// given
		int threadCount = 5;
		User saveduser = userRepository.save(User.create("테스터"));
		pointRepository.save(Point.of(10_000L,saveduser.getId()));

		// when
		PointCommand.Use command = PointCommand.Use.of(saveduser.getId(), 2000L);
		// then
		executor.execute(() -> {
			try {
				pointService.useWithAOPLock("point",command);
			} catch (Exception e) {
				System.out.println("충돌 발생: " + e.getMessage());
			}
		}, threadCount);

		// then
		Point point = pointRepository.findByUserId(saveduser.getId()).orElse(null);

		assertThat(point).isNotNull();
		assertThat(point.getAmount()).isEqualTo(0L);
	}
}
