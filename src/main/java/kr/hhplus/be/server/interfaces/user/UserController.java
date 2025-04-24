package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController implements UserApi {

	private final UserService userService;

	@Override
	@PostMapping
	public ResponseEntity<UserResponse.User> join(@RequestBody UserRequest.Join request) {
		UserInfo.User joinedUser = userService.join(request.toCommand());
		UserResponse.User response = UserResponse.User.of(joinedUser.getId(), joinedUser.getUsername());
		return ResponseEntity.ok(response);
	}
}
