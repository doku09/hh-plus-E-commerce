package kr.hhplus.be.server.interfaces.api.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.user.request.UserJoinRequest;
import kr.hhplus.be.server.interfaces.api.user.response.UserResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User", description = "User 관련 API")
@RequestMapping(value="/api/v1/user")
@RestController
public interface UserApi {

	@Operation(summary = "회원가입",description = "회원가입을 요청한다.")
	@PostMapping
	ResponseEntity<UserResponse> join(@RequestBody UserJoinRequest request);
}
