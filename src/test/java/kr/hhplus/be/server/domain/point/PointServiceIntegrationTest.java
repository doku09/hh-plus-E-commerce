package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class PointServiceIntegrationTest {

	@Autowired
	private PointRepository pointRepository;

	@Autowired
	private PointService pointService;

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("[성공] 기존 회원의 포인트를 충전한다.")
	void user_craetePoint_success() {
		long amount = 3000;

		// 사용자 조회
		User tester = userRepository.save(User.create("tester"));
		pointRepository.save(Point.of(1000L, tester.getId()));
		//충전
		PointInfo.Point result = pointService.charge(PointCommand.Charge.of(tester.getId(), amount));

		assertThat(result.getAmount()).isEqualTo(4000);
	}

	@Test
	@DisplayName("[성공] 포인트를 사용한다.")
	void usePoint_use() {

		// given
		// 사용자 조회
		User tester = userRepository.save(User.create("tester"));
		pointRepository.save(Point.of(1000L, tester.getId()));

		// when
		//포인트 사용
		PointInfo.Point result = pointService.use(PointCommand.Use.of(tester.getId(), 500L));
		// then

		assertThat(result.getAmount()).isEqualTo(500);
	}
}
