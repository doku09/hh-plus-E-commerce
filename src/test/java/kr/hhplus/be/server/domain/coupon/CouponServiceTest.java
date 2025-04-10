package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CouponServiceTest {

	 private LocalDateTime startDate = LocalDateTime.now().minusDays(10);
	 private LocalDateTime endDate = LocalDateTime.now().plusDays(30);

	@Mock
	private CouponRepository couponRepository;

	@InjectMocks
	private CouponService couponService;

	@Test
	@DisplayName("[성공] 수량제한 있는 쿠폰을 등록한다.")
	void make_unlimited_coupon() {

	  // given
		CouponCreateCommand command = CouponCreateCommand.of("깜짝쿠폰", DiscountPolicy.FIXED_AMOUNT, startDate, endDate, 10);
		Coupon savedCoupon = command.toEntity();

		// when
		when(couponRepository.saveCoupon(any(Coupon.class))).thenReturn(savedCoupon);
		couponService.register(command);

	  // then
		assertThat(savedCoupon.canIssue()).isTrue();
		assertThat(savedCoupon.getQuantity()).isEqualTo(10);
	}

	@Test
	@DisplayName("[성공] 수량을 입력하지 않으면 수량제한이 없는 쿠폰이 생성된다.")
	void register_unlimited_coupon() {

	  // given
		CouponCreateCommand command = CouponCreateCommand.of("깜짝쿠폰", DiscountPolicy.FIXED_AMOUNT, startDate, endDate, null);
		Coupon savedCoupon = command.toEntity();

	  // when
		when(couponRepository.saveCoupon(any(Coupon.class))).thenReturn(savedCoupon);
		couponService.register(command);

	  // then
		assertThat(savedCoupon.canIssue()).isTrue();
		assertThat(savedCoupon.getQuantity()).isNull();
	}

	@Test
	@DisplayName("[실패] 쿠폰 개수가 부족하면 에러가발생한다.")
	void not_enough_coupon_exception() {

	  // given
		User user = User.of(1L, "tester");
		Coupon coupon = Coupon.createLimitedCoupon("깜짝쿠폰", DiscountPolicy.FIXED_AMOUNT, startDate, endDate, 0);
		IssueCouponCommand issueCouponCommand = IssueCouponCommand.of(user, coupon);

	  // when & then
		assertThatThrownBy(() -> couponService.issueCoupon(issueCouponCommand))
			.isInstanceOf(GlobalBusinessException.class)
			.hasMessageContaining(ErrorCode.NOT_ENOUGH_COUPON.getMessage());
	}

	@Test
	@DisplayName("[성공] 쿠폰을 발행한다.")
	void issue_coupon_success() {

	  // given
		User user = User.of(1L, "tester");
		Coupon coupon = Coupon.createLimitedCoupon("깜짝쿠폰", DiscountPolicy.FIXED_AMOUNT, startDate, endDate, 5);
		IssueCouponCommand command1 = IssueCouponCommand.of(user, coupon);
		IssueCouponCommand command2 = IssueCouponCommand.of(user, coupon);
		IssueCouponCommand command3 = IssueCouponCommand.of(user, coupon);
		IssueCouponCommand command4 = IssueCouponCommand.of(user, coupon);
		IssueCouponCommand command5 = IssueCouponCommand.of(user, coupon);

	  // when
		couponService.issueCoupon(command1);
		couponService.issueCoupon(command2);
		couponService.issueCoupon(command3);
		couponService.issueCoupon(command4);
		couponService.issueCoupon(command5);

	  // then
		assertThat(coupon.getQuantity()).isEqualTo(0);
	}
}