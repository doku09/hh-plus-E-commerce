package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infrastructure.coupon.CouponJpaRepository;
import kr.hhplus.be.server.infrastructure.coupon.UserCouponJpaRepository;
import kr.hhplus.be.server.infrastructure.user.UserJpaRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;

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
	private UserCouponJpaRepository userCouponJpaRepository;
	@Autowired
	private CouponJpaRepository couponJpaRepository;
	@Autowired
	private UserJpaRepository userJpaRepository;


	@AfterEach
	void tearDown() {
		userCouponJpaRepository.deleteAllInBatch();
		couponJpaRepository.deleteAllInBatch();
		userJpaRepository.deleteAllInBatch();
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
