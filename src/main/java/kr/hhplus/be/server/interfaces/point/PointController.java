package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/point")
public class PointController implements PointApi{

	private final PointService pointService;

	/**
	 * 포인트 충전
	 */
	@Override
	@PostMapping("/charge/{userId}")
	public ResponseEntity<Long> charge(@PathVariable Long userId, PointRequest.Charge request) {
		pointService.charge(request.toCommand(userId));
		return null;
	}
}
