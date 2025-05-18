package kr.hhplus.be.server.infrastructure.coupon;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CouponRedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	public void decrString(String couponKey) {
		redisTemplate.opsForValue().decrement(couponKey);
	}

	public String getString(String couponKey) {
		return Objects.requireNonNull(redisTemplate.opsForValue().get(couponKey)).toString();
	}

	public void setString(String couponKey, String value) {
		redisTemplate.opsForValue().set(couponKey, value);
	}

	public void expire(String couponKey, Duration duration) {
		redisTemplate.expire(couponKey,duration);
	}

	public Long addSet(String key, String value) {
		return redisTemplate.opsForSet().add(key, value);
	}

	public Long execute(DefaultRedisScript<Long> script, List<String> remKey, String s) {
		return redisTemplate.execute(script,remKey,s);
	}
}
