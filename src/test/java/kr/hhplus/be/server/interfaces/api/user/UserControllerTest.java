package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCommand;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
class UserControllerTest {

	@Autowired
	private UserService userService;

	@Test
	@DisplayName("[성공] 유저를 생성한다.")
	void craete_user() {

	  // given
		UserCommand.Join create = UserCommand.Join.of("tester");
		// when
		UserInfo.User joinedUser = userService.join(create);

		// 실제 저장 확인
		User findUser = userService.findById(joinedUser.getId());

		// then
		assertThat(findUser).isNotNull();
		assertThat(joinedUser).isNotNull();
		assertThat(joinedUser.getUsername()).isEqualTo("tester");
	}


}