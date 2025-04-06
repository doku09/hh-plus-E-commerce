package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.NotFountUserException;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final UserRepository userRepository;

	/**
	 * 사용자에게 포인트를 충전합니다.
	 */
	public PointInfo charge(PointChargeCommand command) {

		User findUser = userRepository.findById(command.userId()).orElseThrow(NotFountUserException::new);

		// 회원 포인트를 찾는다 -> 없다 -> 포인트를 하나 만든다. -> 충전 포인트를 더해서 반환한다.
//		Point point = pointRepository.findByUserId()
//		pointRepository.charge();

		return null;
	}

	/**
	 * 사용자에게 포인트를 충전합니다.
	 */
	public Long use(long userId, long amount) {
		return null;
	}

	/**
	 * 사용자의 현재 포인트를 조회합니다.
	 */
	public Long get(Long userId) {
		return null;
	}
}
