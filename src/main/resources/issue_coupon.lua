-- KEYS[1] = remaining key (coupon:{id}:remaining)
-- KEYS[2] = issued key    (coupon:{id}:issued)
-- ARGV[1] = userId

-- 1) 이미 발급됐는지 확인
if redis.call('SISMEMBER', KEYS[2], ARGV[1]) == 1 then
  return -1    -- 이미 발급됨
end

-- 2) 남은 수량 조회
local remain = tonumber(redis.call('GET', KEYS[1]) or '0')
if remain <= 0 then
  return 0     -- 재고 없음
end

-- 3) 재고 차감 + 발급 이력 기록
redis.call('DECR', KEYS[1])
redis.call('SADD', KEYS[2], ARGV[1])

return 1       -- 성공