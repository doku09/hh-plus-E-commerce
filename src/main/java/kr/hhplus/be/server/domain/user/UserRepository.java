package kr.hhplus.be.server.domain.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(Long id);

	List<User> findAll();
}
