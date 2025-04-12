package kr.hhplus.be.server.domain.user;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository {

	User save(User user);

	Optional<User> findById(Long id);
}
