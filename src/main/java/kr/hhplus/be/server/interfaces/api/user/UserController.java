package kr.hhplus.be.server.interfaces.api.user;

import kr.hhplus.be.server.domain.user.UserService;
import kr.hhplus.be.server.interfaces.api.user.request.UserJoinRequest;
import kr.hhplus.be.server.interfaces.api.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class UserController implements UserApi {

	private UserService userService;

	@Override
	public ResponseEntity<UserResponse> join(@RequestBody UserJoinRequest request) {
		UserResponse response = UserResponse.from(userService.join(request.toCommand()));
		return ResponseEntity.ok(response);
	}
}
