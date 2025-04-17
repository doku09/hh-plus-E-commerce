package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor
public class UserCoupon {

	@Column(name="user_coupon_id")
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private long userId;
	private long couponId;
	private UserCouponStatus status;
	private LocalDateTime issuedAt;
	private LocalDateTime usedAt;

	public UserCoupon(long userId, long couponId) {
		this.userId = userId;
		this.couponId = couponId;
		issuedAt = LocalDateTime.now();
	}

	public static UserCoupon createIssuedCoupon(long userId, long couponId) {
		return new UserCoupon(userId, couponId);
	}

	public boolean isSameCoupon(Long couponId) {
		return this.couponId == couponId;
	}

	public void use() {
		if(isUsed()) {
			throw new GlobalBusinessException(ErrorCode.ALREADY_USED_COUPON);
		}

		this.status = UserCouponStatus.USED;
		usedAt = LocalDateTime.now();
	}

	public boolean isUsed() {
		return this.status == UserCouponStatus.USED;
	}
}
