package kr.hhplus.be.server.domain.coupon.event;

public interface CouponEventPublisher {

	void publish(CouponIssuedRequestEvent event);
}
