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
	private final UserRepository userRepository;

	/**
	 * 사용자에게 포인트를 충전합니다.
	 */
	public PointInfo charge(PointChargeCommand command) {

		// TODO QUESTION) UserService의 findById를 호출해야할지 아니면 userRepository를 써야될지
		// Service를 통해 호출하면 파사드를 생성해야 할까요?
		User findUser = userRepository.findById(command.userId()).orElseThrow(NotFoundUserException::new);

		// 회원 포인트를 찾는다 -> 없다? -> 포인트를 하나 만든다. -> 충전 포인트를 더해서 반환한다.
		// orElseGet()을 활용하면 생성된 Point 엔티티를 다시 조회하지 않아도, save된 인스턴스를 바로 사용할 수 있어요.

		Point point = pointRepository.findByUserId(findUser.getId()).orElseGet(
			() -> pointRepository.save(
				Point.builder()
					.amount(Point.ZERO_POINT)
					.user(findUser)
					.build()
			));

		point.charge(command.amount());

		// JPA의 더티체킹이 없다고 간주하고 update
		return PointInfo.from(point);
	}

	/**
	 * 사용자 포인트를 사용합니다.
	 */
	public PointInfo use(PointUseCommand command) {

		// TODO QUESTION) 사용자와 포인트의 관계.. 포인트가 없는 사용자도 있다.. 탈퇴한 사용자라면 포인트데이터도 삭제?? 같이삭제
		// TODO QUETION) 빌터패턴은 인자를 뺴먹어도 생성된다.. 지양해야할까요?

		User findUser = userRepository.findById(command.userId())
			.orElseThrow(NotFoundUserException::new);

		Point point = pointRepository.findByUserId(findUser.getId()).orElseGet(
			() -> pointRepository.save(
				Point.builder()
					.amount(Point.ZERO_POINT)
					.user(findUser)
					.build()
			));

		point.use(command.amount());

		// JPA의 더티체킹이 없다고 간주하고 update
		return PointInfo.from(point);
	}

	/**
	 * 사용자의 현재 포인트를 조회합니다.
	 */
	public Long get(Long userId) {
		return null;
	}
}
