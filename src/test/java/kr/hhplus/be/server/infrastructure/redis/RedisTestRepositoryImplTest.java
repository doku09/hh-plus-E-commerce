package kr.hhplus.be.server.infrastructure.redis;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class RedisTestRepositoryImplTest {

	 @Autowired
	 private RedisTestRepositoryImpl repo;

	 @Test
	 void redis_string() {
		 repo.stringSave("keyTest","항해",100L);

		 Object keyTest = repo.stringGet("keyTest");
		 Boolean stringExists = repo.stringExists("keyTest");

		 System.out.println("keyTest = " + keyTest);
		 System.out.println("stringExists = " + stringExists);
	 }

	@Test
	void redis_hash() {
		repo.hashSave("hashTest", Map.of("name", "항해","age",14));

		Object hashTest = repo.hashGet("hashTest", "name");
		Map<String, Object> maps = repo.hashGetAll("hashTest");

//		repo.hashDelete("hashTest", "name");
	}
}