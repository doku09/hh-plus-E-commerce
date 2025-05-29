package kr.hhplus.be.server.interfaces.coupon;

import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponRequest;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.RequestIssuedCouponCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/coupon")
public class CouponController {

	private final CouponService couponService;

	@PostMapping
	public ResponseEntity<Void> register(@RequestBody CouponRequest.Create request) {

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

	@PostMapping("/request-issue")
	public ResponseEntity<Void> requestIssue(@RequestBody CouponRequest.Issue request) {
		couponService.requestIssuedCoupon(new RequestIssuedCouponCommand(request.getUserId(),request.getCouponId()));
		return ResponseEntity.ok().build();
	}

	@PostMapping("/load-coupon")
	public ResponseEntity<Void> loadCoupon(@RequestBody CouponRequest.Load request) {
		couponService.loadFcFsCoupon(request.getCouponId());

		return ResponseEntity.ok().build();
	}
}
