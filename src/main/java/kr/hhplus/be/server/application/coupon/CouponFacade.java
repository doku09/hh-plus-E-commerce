package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.*;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CouponFacade {

	private final CouponService couponService;
	private final UserService userService;

	// 쿠폰 발행
	@Transactional
	public void issueCoupon(IssueCouponCriteria command) {

		User user = userService.findById(command.getUserId());
		Coupon coupon = couponService.findCouponById(command.getCouponId());

		couponService.issueCoupon(IssueCouponCommand.of(user,coupon));
	}
}
