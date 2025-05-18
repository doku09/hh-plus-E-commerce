package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.redis.RedisRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class CouponServiceIntegrationTest {

	private LocalDateTime startDate = LocalDateTime.now().minusDays(10);
	private LocalDateTime endDate = LocalDateTime.now().plusDays(30);

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private CouponService couponService;

	@Autowired
	private RedisRepository redisRepository;

	@Autowired
	private UserCouponJpaRepository userCouponJpaRepository;
	@Autowired
	private CouponJpaRepository couponJpaRepository;
	@Autowired
	private UserJpaRepository userJpaRepository;
	@Autowired
	private RedisCacheManager redisCacheManager;
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;


	@AfterEach
	void tearDown() {
		userCouponJpaRepository.deleteAllInBatch();
		couponJpaRepository.deleteAllInBatch();
		userJpaRepository.deleteAllInBatch();

		redisCacheManager.getCacheNames().forEach(cacheName -> Objects.requireNonNull(redisCacheManager.getCache(cacheName)).clear());
		redisTemplate.delete(Objects.requireNonNull(redisTemplate.keys("*")));
	}



	@Test
	@DisplayName("쿠폰을 등록한다.")
	void make_unlimited_coupon() {

		// given
		CouponCommand.Create command = CouponCommand.Create.of("깜짝쿠폰", 1000L, 5, CouponType.LIMITED, startDate, endDate);

		//when
		CouponInfo.Coupon result = couponService.register(command);

		//then
		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo("깜짝쿠폰");

	}

	@Test
	@DisplayName("저장한 캐시를 레디스 캐시에 적재한다.")
	@Transactional
	void redis_regist_coupon_with_redis() {

	  // given
		CouponCommand.Create command = CouponCommand.Create.of(
			"깜짝쿠폰",
			1000L,
			100,
			CouponType.LIMITED,
			startDate,
			endDate
		);

	  // when
		CouponInfo.Coupon savedCoupon = couponService.register(command);

		couponService.loadFcFsCoupon(savedCoupon.getId());

		// then
		String couponKey = "coupon:" + savedCoupon.getId() + ":remaining";
		String count = redisRepository.sGet(couponKey);

		assertThat(count).isEqualTo(String.valueOf(savedCoupon.getQuantity()));
	}

	@Test
	@DisplayName("선착순 쿠폰을 레디스 개수기로 발급한다.")
	void issue_coupon_with_redis() {

		// given
		User user = userRepository.save(User.create("테스터"));
		CouponCommand.Create command = CouponCommand.Create.of(
			"깜짝쿠폰",
			1000L,
			100,
			CouponType.LIMITED,
			startDate,
			endDate
		);

		CouponInfo.Coupon savedCoupon = couponService.register(command);
		couponService.loadFcFsCoupon(savedCoupon.getId());

		// when
		UserCouponCommand.Issue issueCommand = UserCouponCommand.Issue.of(savedCoupon.getId(),user.getId());
		couponService.issueCouponWithRedisCounter(issueCommand);

		// then
		String couponKey = "coupon:" + savedCoupon.getId() + ":remaining";
		String issuedKey = "coupon:" + savedCoupon.getId() + ":issued";

		String count = redisRepository.sGet(couponKey);
		Set<Object> issuedUsers = redisRepository.getMembersInSet(issuedKey);

		assertThat(issuedUsers)
			.isNotNull()
			.contains(String.valueOf(user.getId()));

		assertThat(count).isEqualTo(String.valueOf(savedCoupon.getQuantity() - 1));
	}

	@Test
	@DisplayName("쿠폰을 동일한 유저가 중복발행 시 캐시된 쿠폰의 개수가 줄어들지 않는다.")
	void issue_coupon_with_redis_duplicate() {

		// given
		User user = userRepository.save(User.create("테스터"));
		CouponCommand.Create command = CouponCommand.Create.of(
			"깜짝쿠폰",
			1000L,
			100,
			CouponType.LIMITED,
			startDate,
			endDate
		);

		CouponInfo.Coupon savedCoupon = couponService.register(command);
		couponService.loadFcFsCoupon(savedCoupon.getId());

		// when
		UserCouponCommand.Issue issueCommand = UserCouponCommand.Issue.of(savedCoupon.getId(),user.getId());
		couponService.issueCouponWithRedisCounter(issueCommand);
		couponService.issueCouponWithRedisCounter(issueCommand);

		// then
		String couponKey = "coupon:" + savedCoupon.getId() + ":remaining";
		String issuedKey = "coupon:" + savedCoupon.getId() + ":issued";

		String count = redisRepository.sGet(couponKey);
		Set<Object> issuedUsers = redisRepository.getMembersInSet(issuedKey);

		assertThat(issuedUsers)
			.isNotNull()
			.contains(String.valueOf(user.getId()));

		assertThat(count).isEqualTo(String.valueOf(savedCoupon.getQuantity() - 1));
	}

	@Test
	@DisplayName("[성공] 쿠폰을 발행한다.")
	void issue_coupon_success() {

		// given
		long userId = 2L;

		Coupon coupon = couponRepository.saveCoupon(Coupon.create("깜짝쿠폰", 1000L,2,CouponType.LIMITED, startDate, endDate));

		UserCouponCommand.Issue issueCommand = UserCouponCommand.Issue.of(coupon.getId(),userId);

		//when
		Coupon issuedCoupon = couponService.issueCoupon(issueCommand);

		//then
		assertThat(issuedCoupon.getQuantity()).isEqualTo(1);
	}

	@Test
	@DisplayName("[성공] 쿠폰을 사용한다.")
	void useCoupon() {

		// given
		String couponName = "깜짝쿠폰";
		//유저 생성
		User tester = userRepository.save(User.create("tester"));
		// 쿠폰 생성
		Coupon coupon = couponRepository.saveCoupon(Coupon.create("깜짝쿠폰", 1000L,2,CouponType.LIMITED, startDate, endDate));
		UserCouponCommand.Issue issueCommand = UserCouponCommand.Issue.of(coupon.getId(),tester.getId());
		couponService.issueCoupon(issueCommand);

		//when
		CouponCommand.Use use = CouponCommand.Use.of(tester.getId(), coupon.getId());
		Coupon usedCoupon = couponService.useCoupon(use);

		// then
		assertThat(usedCoupon.getName()).isEqualTo(couponName);
	}
}
