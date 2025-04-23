package kr.hhplus.be.server.domain.coupon;

import lombok.Getter;

public class UserCouponCommand {

	@Getter
	public static class Issue {
		private Long couponId;
		private Long userId;

		private Issue(Long couponId, Long userId) {
			this.couponId = couponId;
			this.userId = userId;
		}

		public static Issue of(Long couponId, Long userId) {
			return new Issue(couponId, userId);
		}
	}


}
