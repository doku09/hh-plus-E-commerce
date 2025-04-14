package kr.hhplus.be.server.infrastructure.user;

import kr.hhplus.be.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository repository;
}
