package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.concurrent.ConcurrencyExecutor;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PointDistributedLockTransactionServiceTest {
	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ConcurrencyExecutor executor;

	@Autowired
	private PointDistributedLockService pointDistributedLockService;


	@Test
	@Order(1)
	@DisplayName("[성공] simpleLock 분산락으로 포인트 사용 동시성을 제어한다.")
	@DirtiesContext
	void simplLock_point_use() {

		// given
		int threadCount = 5;

		User saveduser = userRepository.save(User.create("테스터"));
		pointRepository.save(Point.of(10_000L,saveduser.getId()));

		// when
		PointCommand.Use command = PointCommand.Use.of(saveduser.getId(), 2000L);
		// then
		executor.execute(() -> {
			try {
				pointDistributedLockService.useWithSimpleLock(command);
			} catch (Exception e) {
				System.out.println("충돌 발생: " + e.getMessage());
			}
		}, threadCount);

		// then
		Point point = pointRepository.findByUserId(saveduser.getId()).orElse(null);

		assertThat(point).isNotNull();
		assertThat(point.getAmount()).isEqualTo(8000L);
	}
}