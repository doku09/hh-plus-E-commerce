package kr.hhplus.be.server.infrastructure.redis;

import org.springframework.data.repository.CrudRepository;

public interface RedisDataRepositoryImpl extends CrudRepository<Person, String> {
}
