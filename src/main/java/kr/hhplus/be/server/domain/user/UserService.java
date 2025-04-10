package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.common.exception.NotFoundUserException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;

	/**
	 * 회원가입
	 */
	public UserInfo join(UserJoinCommand userJoinCommand) {
		return UserInfo.from(userRepository.save(userJoinCommand.toEntity()));
	}

	public User findById(Long id) {
		return userRepository.findById(id).orElseThrow(NotFoundUserException::new);
	}
}
