package kr.hhplus.be.server.infrastructure.bestItem;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.time.Duration;

@Repository
@RequiredArgsConstructor
public class BestItemRedisRepository {

	private final RedisTemplate<String,String> redisTemplate;

	public void incrSortedSet(String key, String value, double quantity, Duration ttl) {
		redisTemplate.opsForZSet().incrementScore(key,value,quantity);
		redisTemplate.expire(key,ttl);
	}
}
