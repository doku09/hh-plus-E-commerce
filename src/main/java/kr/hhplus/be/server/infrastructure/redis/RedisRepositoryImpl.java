package kr.hhplus.be.server.infrastructure.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisRepositoryImpl implements RedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ValueOperations<String, Object> valueOps;
	private final HashOperations<String, String, Object> hashOps;
	private final ListOperations<String, Object> listOps;
	private final SetOperations<String, Object> setOps;
	private final ZSetOperations<String, Object> zSetOps;

	@Autowired
	public RedisRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.valueOps    = redisTemplate.opsForValue();
		this.hashOps     = redisTemplate.opsForHash();
		this.listOps     = redisTemplate.opsForList();
		this.setOps      = redisTemplate.opsForSet();
		this.zSetOps     = redisTemplate.opsForZSet();
	}
	@Override
	public void incrSortedSet(String key, String value, double quantity,Duration ttl) {
		zSetOps.incrementScore(key,value,quantity);
		redisTemplate.expire(key,ttl);
	}

	@Override
	public void addSortedSetWithTTL(String key, String value, double score, Duration ttl) {
		zSetOps.add(key,value,score);
		redisTemplate.expire(key,ttl);
	}

	@Override
	public Set<Object> getSoretedSetReverseRange(String key, int limit) {
		return zSetOps.reverseRange(key,0,limit);
	}

	@Override
	public void sSet(String key, String value) {
		valueOps.set(key,value);
	}

	@Override
	public void sIncr(String key) {
		valueOps.increment(key);
	}

	@Override
	public void sDecr(String key) {
		valueOps.decrement(key);
	}

	@Override
	public String sGet(String key) {
		return (String) valueOps.get(key);
	}

	@Override
	public Set<Object> getMembersInSet(String key) {
		return setOps.members(key);
	}

	@Override
	public Long execute(DefaultRedisScript<Long> script, List<String> remKey, String s) {
		return redisTemplate.execute(script,remKey,s);
	}

	@Override
	public Boolean expire(String key, Long duration, TimeUnit timeUnit) {
		return redisTemplate.expire(key,duration,timeUnit);
	}

	@Override
	public Boolean expire(String key, Duration ttl) {
		return redisTemplate.expire(key,ttl);
	}

	@Override
	public Long getExpire(String key) {
		return redisTemplate.getExpire(key);
	}

	@Override
	public void expireAt(String key, Date date) {
		redisTemplate.expireAt(key,date);
	}

	@Override
	public Set<Object> reverseRange(String key, Long start, Long end) {

		return zSetOps.reverseRange(key,start,end);
	}

	@Override
	public Long rightPushAll(String key, List<String> values) {
		return listOps.rightPushAll(key,values);
	}

	@Override
	public Object leftPop(String key) {
		return listOps.leftPop(key);
	}

	@Override
	public Long addSet(String key, String value) {
		return setOps.add(key,value);
	}
}
