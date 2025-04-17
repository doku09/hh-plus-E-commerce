package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponRequest;
import kr.hhplus.be.server.domain.coupon.CouponService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {

	private CouponService couponService;

	@PostMapping
	public ResponseEntity<Void> register(CouponRequest.Create request) {

		CouponCommand.Create command = CouponCommand.Create.of(
			request.getName(),
			request.getDiscountPrice(),
			request.getQuantity(),
			request.getCouponType(),
			request.getUseStartDate(),
			request.getExpiredDate()
		);

		couponService.register(command);

		return ResponseEntity.ok().build();
	}


}
