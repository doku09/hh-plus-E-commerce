package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.NotFoundUserException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;

	/**
	 * 사용자에게 포인트를 충전합니다.
	 */
	public PointInfo.Point charge(PointCommand.Charge command) {

		Point point = pointRepository.findByUserId(command.getUserId()).orElseGet(
			() -> pointRepository.save(Point.of(Point.ZERO_POINT,command.getUserId())));

		point.charge(command.getAmount());

		return PointInfo.Point.of(point.getAmount());
	}

	/**
	 * 사용자 포인트를 사용합니다.
	 */
	public PointInfo.Point use(PointCommand.Use command) {

		Point point = pointRepository.findByUserId(command.getUserId()).orElseGet(
			() -> pointRepository.save(
				Point.of(Point.ZERO_POINT, command.getUserId()
			)));

		point.use(command.getAmount());

		return PointInfo.Point.of(point.getAmount());
	}

	/**
	 * 사용자의 현재 포인트를 조회합니다.
	 */
	public Point get(Long userId) {
		return pointRepository.findByUserId(userId)
			.orElseThrow(NotFoundUserException::new);

	}
}
