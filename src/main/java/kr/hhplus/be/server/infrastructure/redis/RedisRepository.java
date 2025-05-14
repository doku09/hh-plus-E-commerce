package kr.hhplus.be.server.infrastructure.redis;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public interface RedisRepository {
	void incrSortedSet(String key, String value, double quantity, Duration ttl);

	Boolean expire(String key, Long duration, TimeUnit timeUnit);

	Long getExpire(String key);

	void expireAt(String key, Date date);

	Set<Object> reverseRange(String key, Long start, Long end);

	Long rightPushAll(String key, List<String> values);

	Object leftPop(String key);

	Long addSet(String key, String value);

	void addSortedSetWithTTL(String key, String value, double score, Duration ttl);

	Set<Object> getSoretedSetReverseRange(String key, int limit);
}
