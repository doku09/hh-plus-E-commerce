package kr.hhplus.be.server.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@InjectMocks
	private UserService userService;

	@Test
	@DisplayName("[성공] 회원가입에 성공한다.")
	void join() {

		// given
		String name = "테스트 계정";

		UserCommand.Join joinCommand = UserCommand.Join.of(name);
		// when
		when(userRepository.save(any(User.class)))
			.thenReturn(new User(1L,name));

		UserInfo.User joinedUser = userService.join(joinCommand);

		// then
		assertThat(joinedUser.getId()).isEqualTo(1L);
		assertThat(joinedUser.getUsername()).isEqualTo(name);
	}
}