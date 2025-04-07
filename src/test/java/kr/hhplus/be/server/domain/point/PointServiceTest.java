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

	@Mock
	private UserRepository userRepository;

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
			when(userRepository.findById(userId)).thenReturn(Optional.of(User.builder().build()));

			//  then
			assertThatThrownBy(() -> pointService.charge(command))
				.isInstanceOf(MaxPointException.class);
		}

		@Test
		@DisplayName("[실패] 충전하려는 회원정보가 없다.")
		void charge_NotfoundUser() {

		  // given
			long userId = 2L;
			long amount = 1000L;

			PointChargeCommand command = PointChargeCommand.builder()
				.userId(userId)
				.amount(amount)
				.build();

			// when
			when(userRepository.findById(userId)).thenReturn(Optional.empty());

		  // then
			assertThatThrownBy(() ->pointService.charge(command))
				.isInstanceOf(NotFoundUserException.class);
			verify(pointRepository,never()).findByUserId(userId);
		}

		@Test
		@DisplayName("[성공] 회원이 포인트를 보유하고 있지않으면 포인트를 생성한다.")
		void userNotPoint_craetePoint() {
			long userId = 3L;
			long amount = 3000;

			User user = new User(userId, "tester");

			Point craeted = Point.builder()
				.user(user)
				.amount(Point.ZERO_POINT)
				.build();

			Point afterCharge = Point.builder()
				.user(user)
				.amount(amount)
				.build();

			when(userRepository.findById(userId)).thenReturn(Optional.of(user));
			when(pointRepository.findByUserId(userId)).thenReturn(Optional.empty());
			when(pointRepository.save(any(Point.class))).thenReturn(craeted);
			when(pointRepository.update(any(Point.class))).thenReturn(afterCharge);

			PointChargeCommand command = PointChargeCommand.builder()
				.userId(userId)
				.amount(amount)
				.build();

			PointInfo result = pointService.charge(command);

			assertThat(result.amount()).isEqualTo(amount);
		}

		@Test
		@DisplayName("[성공] 기존 회원의 포인트를 충전한다.")
		void user_craetePoint_success() {
			long userId = 3L;
			long amount = 3000;

			User user = new User(userId, "tester");

			Point userPoint = Point.builder()
				.user(user)
				.amount(1000)
				.build();

			Point afterCharge = Point.builder()
				.user(user)
				.amount(amount+1000)
				.build();

			when(userRepository.findById(userId)).thenReturn(Optional.of(user));
			when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(userPoint));
			when(pointRepository.update(any(Point.class))).thenReturn(afterCharge);

			PointChargeCommand command = PointChargeCommand.builder()
				.userId(userId)
				.amount(amount)
				.build();

			PointInfo result = pointService.charge(command);

			assertThat(result.amount()).isEqualTo(4000);
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

		  // then
			assertThatThrownBy(() -> pointService.use(PointUseCommand.builder()
				.userId(userId)
				.amount(amount)
				.build()
			)).isInstanceOf(NotEnoughPointException.class);
		}
		
		@Test
		@DisplayName("[실패] 포인트를 충전하려는 사용자 정보가 없으면 예외발생")
		void use_notFoundUser_exception() {
			
		  // given
			long userId = 4L;
			long amount = 3000L;

		  // when
			when(userRepository.findById(userId)).thenReturn(Optional.empty());
		
		  // then
			assertThatThrownBy(() -> pointService.use(PointUseCommand.builder()
				.userId(userId)
				.amount(amount)
				.build()
			)).isInstanceOf(NotFoundUserException.class);
		}

		@Test
		@DisplayName("[성공] 포인트를 사용하면 기존포인트에서 차감한다.")
		void usePoint_use() {

		  // given
			long userId = 4L;
			long amount = 500L;
			User user = new User(userId, "tester");

			Point userPoint = Point.builder()
				.user(user)
				.amount(1000)
				.build();

		  // when
			when(userRepository.findById(userId)).thenReturn(Optional.of(user));
			when(pointRepository.findByUserId(userId)).thenReturn(Optional.of(userPoint));


			// then
			PointInfo result = pointService.use(
				PointUseCommand.builder()
					.userId(userId)
					.amount(amount)
					.build()
			);

			assertThat(result.amount()).isEqualTo(500);
		}
	}
}