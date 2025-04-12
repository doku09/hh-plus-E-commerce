package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.point.request.PointChargeRequest;
import kr.hhplus.be.server.interfaces.api.point.request.PointUseRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Point", description = "회원 포인트 관련 API")
@RequestMapping(value = "/api/v1/point")
public interface PointApi {

	@Operation(summary = "포인트 충전", description = "회원의 잔액을 특정 금액만큼 충전합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "충전 완료"),
		@ApiResponse(responseCode = "400", description = "잘못된 요청 (음수 충전, 유저 ID 누락 등)"),
		@ApiResponse(responseCode = "404", description = "해당 유저를 찾을 수 없음")
	})
	@PostMapping("/charge")
	ResponseEntity<Long> charge(@io.swagger.v3.oas.annotations.parameters.RequestBody(
		description = "충전 요청 예시",
		required = true,
		content = @Content(
			mediaType = "application/json",
			examples = {
				@ExampleObject(
					name = "정상 요청",
					summary = "10000 포인트 충전",
					value = """
						{
							"userId":1,
							"amount":10000
						}
						"""
				),
				@ExampleObject(
					name = "음수 요청",
					summary = "잘못된 요청 - 음수 금액",
					value = """
						{
							"userId":1,
							"amount": -5000
						}
						"""
				),
				@ExampleObject(
					name = "유저 ID 누락",
					summary = "잘못된 요청 -useId 누락",
					value = """
						{
							"amount":10000
						}
						"""
				)
			}
		)
	) @RequestBody PointChargeRequest request);


	@Operation(summary = "포인트 사용", description = "회원이 포인트를 사용합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "사용 완료")
	})
	@PostMapping("/use")
	ResponseEntity<Long> use(@RequestBody PointUseRequest request);


	@Operation(summary = "포인트 조회", description = "특정 회원의 포인트을 조회합니다.")
	@ApiResponses({
		@ApiResponse(responseCode = "200", description = "포인트 반환")
	})
	@GetMapping("/{userId}")
	ResponseEntity<Long> get(@PathVariable Long userId);
}
