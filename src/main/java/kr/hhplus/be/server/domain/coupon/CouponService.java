package kr.hhplus.be.server.domain.coupon;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final CouponRepository couponRepository;

	// 쿠폰 등록
	public Coupon register(CouponCreateCommand command) {

		return couponRepository.saveCoupon(command.toEntity());
	}

	public Coupon findCouponById(long couponId) {
		return couponRepository.findCouponById(couponId).orElseThrow(() -> new GlobalBusinessException(ErrorCode.NOT_FOUND_COUPON));
	}

	// 쿠폰 발행
	public void issueCoupon(IssueCouponCommand command) {

		command.getCoupon().issue();

		IssuedCoupon issuedCoupon = IssuedCoupon.createIssuedCoupon(command.getUser(), command.getCoupon());
		couponRepository.issueCoupon(issuedCoupon);
	}


}
