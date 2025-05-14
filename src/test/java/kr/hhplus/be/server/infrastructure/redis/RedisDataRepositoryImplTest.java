package kr.hhplus.be.server.infrastructure.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RedisDataRepositoryImplTest {

	@Autowired
	private RedisDataRepositoryImpl repo;

	@Test
	void test() {
		Person person = new Person("Yu");

		repo.save(person);

		repo.findById(person.getId());

		repo.count();

//		repo.delete(person);
	}
  
}