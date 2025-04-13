package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.MaxPointException;
import kr.hhplus.be.server.common.exception.NotEnoughPointException;
import kr.hhplus.be.server.common.exception.NotFoundUserException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

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

			when(pointRepository.findByUserId(userId))
				.thenReturn(Optional.of(Point.of(0L, userId)));

			PointCommand.Charge command = PointCommand.Charge.of(userId, amount);

			//  then
			assertThatThrownBy(() -> pointService.charge(command))
				.isInstanceOf(MaxPointException.class);
		}


		@Test
		@DisplayName("[성공] 기존 회원의 포인트를 충전한다.")
		void user_craetePoint_success() {
			long userId = 3L;
			long amount = 3000;

			Point userPoint = Point.of(1000L, userId);

			when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(userPoint));

			PointCommand.Charge command = PointCommand.Charge.of(userId, amount);

			PointInfo.Point result = pointService.charge(command);

			assertThat(result.getAmount()).isEqualTo(4000);
		}
	}

	@Nested
	class PointUse {
		@Test
		@DisplayName("[실패] 사용하려는 포인트 잔액보다 많으면 예외를 던진다.")
		void usePoint_moreOrigin_throwException() {

		  // given
			long userId = 4L;
			long amount = 3000L;

		  // when
		  // 사용자가 2000원 보유하고 있는데
			when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(Point.of(2000L, userId)));

			// 3000원 사용했을 경우
			PointCommand.Use command = PointCommand.Use.of(userId, amount);

		  // then
			// 예외발생
			assertThatThrownBy(() -> pointService.use(command))
				.isInstanceOf(NotEnoughPointException.class);
		}

		@Test
		@DisplayName("[성공] 포인트를 사용하면 기존포인트에서 차감한다.")
		void usePoint_use() {

		  // given
			long userId = 4L;
			long amount = 500L;
			PointCommand.Use command = PointCommand.Use.of(userId, amount);

		  // when
			when(pointRepository.findByUserId(anyLong())).thenReturn(Optional.of(Point.of(1000L, userId)));

			// then
			PointInfo.Point result = pointService.use(command);
			assertThat(result.getAmount()).isEqualTo(500);
		}
	}
}