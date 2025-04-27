package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.NotEnoughPointException;
import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.annotation.DirtiesContext.*;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
public class PointConcurrencyTest {

	@Autowired
	private PointRepository pointRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConcurrencyExecutor executor;
	@Autowired
	private PointService pointService;

	private final int THREAD_COUNT = 5;

	@Test
	@DisplayName("[성공] 동시에 포인트를 사용하면 재시도하여 성공한다.")
	void concurrency_use_point() {

	  // given
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
		}, THREAD_COUNT);

	  // then
		Point point = pointRepository.findByUserId(saveduser.getId()).orElse(null);

		assertThat(point).isNotNull();
		assertThat(point.getAmount()).isEqualTo(50_000L);
	}

	@Test
	@DisplayName("[성공] simpleLock 분산락으로 포인트 사용 동시성을 제어한다.")
	void simplLock_point_use() {

	  // given
		User saveduser = userRepository.save(User.create("테스터"));
		pointRepository.save(Point.of(10_000L,saveduser.getId()));

	  // when
		PointCommand.Use command = PointCommand.Use.of(saveduser.getId(), 2000L);
	  // then
		executor.execute(() -> {
			try {
				pointService.use(command);
			} catch (NotEnoughPointException e) {
				System.out.println("충돌 발생: " + e.getMessage());
			}
		}, THREAD_COUNT);

		// then
		Point point = pointRepository.findByUserId(saveduser.getId()).orElse(null);

		assertThat(point).isNotNull();
		assertThat(point.getAmount()).isEqualTo(0L);
	}
}
