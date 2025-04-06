package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.interfaces.api.point.request.PointChargeRequest;
import lombok.Builder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

	@Mock
	private PointRepository pointRepository;

	@InjectMocks
	private PointService pointService;

	@Nested
	class PointCharge {
		@Test
		@DisplayName("[실패] 충전 포인트가 최대포인트를 넘어가면 실패한다.")
		void charge_maxPoint_fail() {

			// given
			long userId = 1L;
			long amount = Point.MAX_POINT + 1;
			PointChargeCommand command = PointChargeCommand.builder()
				.userId(userId)
				.amount(amount)
				.build();

			// when
			pointService.charge(command);
			// then
		}
	}
}