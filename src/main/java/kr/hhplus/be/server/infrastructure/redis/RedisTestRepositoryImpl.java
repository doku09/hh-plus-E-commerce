package kr.hhplus.be.server.infrastructure.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Repository
public class RedisTestRepositoryImpl {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ValueOperations<String, Object> valueOps;
	private final HashOperations<String, String, Object> hashOps;
	private final ListOperations<String, Object> listOps;
	private final SetOperations<String, Object> setOps;
	private final ZSetOperations<String, Object> zSetOps;

	@Autowired
	public RedisTestRepositoryImpl(RedisTemplate<String, Object> redisTemplate) {
		this.redisTemplate = redisTemplate;
		this.valueOps    = redisTemplate.opsForValue();
		this.hashOps     = redisTemplate.opsForHash();
		this.listOps     = redisTemplate.opsForList();
		this.setOps      = redisTemplate.opsForSet();
		this.zSetOps     = redisTemplate.opsForZSet();
	}

	//================================================================
	// 1) String 자료구조 CRUD
	//================================================================
	public void stringSave(String key, Object value, long ttlSeconds) {
		valueOps.set(key, value, ttlSeconds, TimeUnit.SECONDS);
	}

	public Object stringGet(String key) {
		return valueOps.get(key);
	}

	public Boolean stringExists(String key) {
		return redisTemplate.hasKey(key);
	}

	public void stringDelete(String key) {
		redisTemplate.delete(key);
	}

	//================================================================
	// 2) Hash 자료구조 CRUD
	//================================================================
	public void hashSave(String key, Map<String, Object> map) {
		hashOps.putAll(key, map);
	}

	public Object hashGet(String key, String field) {
		return hashOps.get(key, field);
	}

	public Map<String, Object> hashGetAll(String key) {
		return hashOps.entries(key);
	}

	public void hashDelete(String key, String... fields) {
		hashOps.delete(key, (Object[]) fields);
	}

	//================================================================
	// 3) List 자료구조 CRUD
	//================================================================
	public void listLeftPush(String key, Object value) {
		listOps.leftPush(key, value);
	}

	public Object listRightPop(String key) {
		return listOps.rightPop(key);
	}

	public List<Object> listRange(String key, long start, long end) {
		return listOps.range(key, start, end);
	}

	public void listTrim(String key, long start, long end) {
		listOps.trim(key, start, end);
	}

	//================================================================
	// 4) Set 자료구조 CRUD
	//================================================================
	public void setAdd(String key, Object... values) {
		setOps.add(key, values);
	}

	public Set<Object> setMembers(String key) {
		return setOps.members(key);
	}

	public Boolean setIsMember(String key, Object value) {
		return setOps.isMember(key, value);
	}

	public void setRemove(String key, Object... values) {
		setOps.remove(key, values);
	}

	//================================================================
	// 5) ZSet (Sorted Set) 자료구조 CRUD
	//================================================================
	public void zSetAdd(String key, Object value, double score) {
		zSetOps.add(key, value, score);
	}

	public Set<Object> zSetRangeByScore(String key, double min, double max) {
		return zSetOps.rangeByScore(key, min, max);
	}

	public void zSetRemove(String key, Object... values) {
		zSetOps.remove(key, values);
	}

	//================================================================
	// 6) 공통 TTL 설정 / 조회
	//================================================================
	public Boolean expire(String key, long ttlSeconds) {
		return redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
	}

	public Long getExpire(String key) {
		return redisTemplate.getExpire(key, TimeUnit.SECONDS);
	}
}
