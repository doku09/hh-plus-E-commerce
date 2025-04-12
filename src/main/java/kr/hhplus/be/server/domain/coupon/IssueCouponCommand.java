package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class IssueCouponCommand {

	private Coupon coupon;
	private User user;

	private IssueCouponCommand(User user,Coupon coupon) {
		this.coupon = coupon;
		this.user = user;
	}

	public static IssueCouponCommand of(User user, Coupon coupon) {
		return new IssueCouponCommand(user,coupon);
	}
}
