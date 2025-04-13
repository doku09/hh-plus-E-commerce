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
import java.util.Optional;

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
		CouponCommand.Create command = CouponCommand.Create.of("깜짝쿠폰", DiscountPolicy.FIXED_AMOUNT, 0, CouponType.LIMITED, startDate, endDate);

		//when
		CouponInfo.Coupon result = couponService.register(command);

		//then
		assertThat(result).isNotNull();

	}

	@Test
	@DisplayName("[실패] 쿠폰 개수가 부족하면 에러가발생한다.")
	void not_enough_coupon_exception() {

	  // given
		long userId = 1L;
		long couponId = 1L;

		Coupon coupon = Coupon.create("깜짝쿠폰", DiscountPolicy.FIXED_AMOUNT,0,CouponType.LIMITED, startDate, endDate);

		IssueCouponCommand.Issue issueCommand = IssueCouponCommand.Issue.of(userId, couponId);

		//when

		when(couponRepository.findCouponById(couponId)).thenReturn(Optional.of(coupon));

		// when & then
		assertThatThrownBy(() -> couponService.issueCoupon(issueCommand))
			.isInstanceOf(GlobalBusinessException.class)
			.hasMessageContaining(ErrorCode.NOT_ENOUGH_COUPON.getMessage());
	}

	@Test
	@DisplayName("[성공] 쿠폰을 발행한다.")
	void issue_coupon_success() {

		// given
		long userId = 1L;
		long couponId = 1L;

		Coupon coupon = Coupon.create("깜짝쿠폰", DiscountPolicy.FIXED_AMOUNT,2,CouponType.LIMITED, startDate, endDate);

		IssueCouponCommand.Issue issueCommand = IssueCouponCommand.Issue.of(userId, couponId);

		//when
		when(couponRepository.findCouponById(couponId)).thenReturn(Optional.of(coupon));
		couponService.issueCoupon(issueCommand);

		//then
		assertThat(coupon.getQuantity()).isEqualTo(1);

	}
}