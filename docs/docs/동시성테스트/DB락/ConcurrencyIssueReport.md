# 📄 동시성 이슈 식별 및 DB Lock 전략 적용 보고서

---

## ✅ 주요 동시성 이슈 식별

| 구분                  | 시나리오 설명                                                                              | 문제 식별                                                                     |
|---------------------|--------------------------------------------------------------------------------------|---------------------------------------------------------------------------|
| 포인트 사용              | - 사용자 A가 100,000P 보유<br>- 동시에 5,000P씩 10번 사용<br>- 기대 잔액: 50,000P<br>- 실제 잔액: 80,000P | Optimistic Lock 충돌: 동시 수정으로 인해 누락 발생                                      |
| 상품 재고 차감            | - 재고 5개<br>- 3개의 스레드가 동시에 차감<br>- 기대 결과: 2개 남음<br>- 실제 결과: 1개만 차감됨                   | Update 충돌로 인해 일부 요청 반영 안됨                                                 |
| 동일한 사용자에게 동일한 쿠폰 발급 | - 동일 유저가 동시에 쿠폰 3개 요청<br>- 비즈니스상 1개만 허용<br>- 기대 발급 수: 1<br>- 실제 발급 수: 3개             | 중복 발급 허용됨: 유효성 검사 시점 불일치                                                  |
| 선착순 쿠폰 발급           | - 동시에 여러 스레드가 쿠폰 갯수에 접근하여 **갱신분실(lost update)** 발생                                   | - 10개의 쿠폰이 있고, 동시에 10명의 사용자가 요청하였다.    <br/>- 쿠폰은 정상적으로 지급이 되었으나 쿠폰은 남아있음 |

---

## 🔧 동시성 이슈 해결을 위한 DB Lock 전략

| 전략 유형             | 설명                                                                                         | 적용 도메인        |
|----------------------|----------------------------------------------------------------------------------------------|---------------|
| 🧱 **Pessimistic Lock (비관적 락)** | - `SELECT ... FOR UPDATE` 사용<br>- 트랜잭션 내에서 다른 트랜잭션 접근 차단                              | 쿠폰 선착순 발급     |
| 📈 **Optimistic Lock (낙관적 락)**  | - `@Version` 필드 기반 동시 수정 감지<br>- 수정 충돌 시 예외 발생 후 재시도                                 | 포인트 사용, 재고 차감 |

---

## 📋 해결

### 1️⃣ 포인트 사용

#### 🔍 문제 발생 로그
```bash
Hibernate: select p1_0.id, p1_0.amount, p1_0.user_id, p1_0.version from point p1_0 where p1_0.id=?
충돌 발생: Row was updated or deleted by another transaction...
```

✅ 개선 방안 - Optimistic Lock 적용
```java
@Retryable(
retryFor = {
  OptimisticLockException.class,
  StaleObjectStateException.class,
  ObjectOptimisticLockingFailureException.class
},
maxAttempts = 5,
backoff = @Backoff(delay = 100)
)
@Transactional
public PointInfo.Point use(PointCommand.Use command) {
...
}
```

### 2️⃣ 상품 재고 차감
🔍 문제 발생 로그
``` bash
Hibernate: update product_stock set ...  (3건 동시 발생)
Expected: 5
Actual  : 4
```

✅ 개선 방안 - Optimistic Lock + @Retryable
- @Version 필드 추가 후, 재고 차감 시 충돌 감지
- 충돌 시 자동 재시도 처리
---
### 3️⃣ 쿠폰 선착순 발급
- 동시에 10개의 사용자가 선착순 쿠폰 발급 요청  

🔍 문제 발생 로그


``` bash
expected: 0
 but was: 6
```
✅ 개선 방안 - Pessimistic Lock 적용
- 쿠폰 조회 시점에 select for update 사용
```java
@Lock(LockModeType.PESSIMISTIC_WRITE)
@Query("SELECT c FROM Coupon c WHERE c.id = :id")
Optional<Coupon> findByIdUpdate(@Param("id") Long id);
```