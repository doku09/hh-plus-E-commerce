package kr.hhplus.be.server.domain.user;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserRepository {

	User save(User user);

	Optional<User> findById(Long id);
}
