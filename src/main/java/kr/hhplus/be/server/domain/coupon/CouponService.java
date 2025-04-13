package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;

	// 쿠폰 등록
	public CouponInfo.Coupon register(CouponCommand.Create command) {
		Coupon coupon = Coupon.create(command.getName(), command.getDiscountPolicy(), command.getQuantity(),command.getCouponType() ,command.getUseStartDate(), command.getExpiredDate());

		Coupon savedCoupon = couponRepository.saveCoupon(coupon);

		return CouponInfo.Coupon.of(
				savedCoupon.getName(),
				savedCoupon.getUseStartDate(),
				savedCoupon.getExpiredDate(),
				savedCoupon.getQuantity(),
				savedCoupon.getCouponType()
		);
	}

	public Coupon findCouponById(long couponId) {
		return couponRepository.findCouponById(couponId).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));
	}

	public void issueCoupon(IssueCouponCommand.Issue command) {

		Coupon findCoupon = couponRepository.findCouponById(command.getCouponId()).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));

		List<IssuedCoupon> userCouponIds = couponRepository.findIssuedCouponByUserId(command.getUserId());

		boolean hasCoupon = userCouponIds.stream().anyMatch(issuedCoupon -> issuedCoupon.getCouponId() == command.getCouponId());

		if (hasCoupon) {
			throw new GlobalBusinessException(ErrorCode.ALREADY_ISSUED_COUPON);
		}

		findCoupon.issue();

		IssuedCoupon issuedCoupon = IssuedCoupon.createIssuedCoupon(command.getUserId(), command.getCouponId());

		couponRepository.issueCoupon(issuedCoupon);
	}
}
