package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.domain.point.PointInfo;
import kr.hhplus.be.server.domain.point.PointService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/point")
@RequiredArgsConstructor
public class PointController implements PointApi{
	private final PointService pointService;

	@Override
	public ResponseEntity<Long> charge(PointChargeRequest request) {
		PointInfo pointInfo = pointService.charge(PointChargeRequest.toCommand(request));
		//TODO 수정
		return ResponseEntity.ok(null);
	}

	@Override
	public ResponseEntity<Long> use(PointUseRequest request) {

//		return ResponseEntity.ok(pointService.use(PointUseRequest);
		return null;
	}

	@Override
	public ResponseEntity<Long> get(Long userId) {
		return ResponseEntity.ok(pointService.get(userId));
	}
}
