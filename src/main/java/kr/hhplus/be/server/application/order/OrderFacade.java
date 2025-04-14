package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponInfo;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStockCommand;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;
	private final ProductService productService;
	private final PointService pointService;
	private final CouponService couponService;

	@Transactional
	public OrderResult.Order order(OrderCriteria.CreateOrder criteria) {
		OrderCommand.Create orderCreateCommand = criteria.toCommand();

		Order order = Order.createOrder(criteria.getUserId());

		// 주문아이템 (상품정보, 개수) 루프 돌면서 저장 후 Order 저장
		for (OrderCriteria.OrderItem orderProduct : criteria.getOrderItems()) {
			ProductInfo.Product product = productService.findById(orderProduct.getProductId());

			//주문아이템 추가
			order.addItem(OrderItem.of(product.getId(), product.getPrice(), orderProduct.getQuantity()));

			//주문 재고 차감 -> 재고 부족 시 주문상태 변경
			try {
				productService.deductStock(ProductStockCommand.Deduct.of(product.getId(), orderProduct.getQuantity()));
			} catch (GlobalBusinessException e) {
				if (e.getMessage().equals(ErrorCode.NOT_ENOUGH_STOCK.getMessage())) {
					//상품 재고에 문제있을 경우 주문 상태 "취소"로 변경
					order.changeStatus(OrderStatus.CANCELED);
				}
			}
		}

    // 쿠폰 사용
	CouponInfo.Coupon coupon = Optional.ofNullable(criteria.getCouponId())
			.map(couponId -> {
				CouponCommand.Use useCouponCommand = CouponCommand.Use.of(criteria.getUserId(), couponId);
				return couponService.useCoupon(useCouponCommand);
			}).orElse(null);

		if(null != coupon) {
			// TODO QUESTION) 쿠폰 사용 시 서비스 응답DTO를 이렇게 다시 도메인 객체로 만드는게 맞나?
			Coupon findCoupon = Coupon.of(
				coupon.getId(),
				coupon.getName(),
				coupon.getDiscountPrice(),
				coupon.getQuantity(),
				coupon.getCouponType(),
				coupon.getUseStartDate(),
				coupon.getExpiredDate()
			);
			// 협력대상을 알게하기 위해 coupon을 넘겨줌
			order.applyCoupon(findCoupon);
		}

		// 주문 처리
		OrderInfo.Order orderInfo = orderService.order(order);

		// 포인트 차감
		pointService.use(PointCommand.Use.of(orderCreateCommand.getUserId(),order.getTotalPrice()));

		return OrderResult.Order.of(orderInfo.getId(),orderInfo.getTotalPrice(),orderInfo.getStatus());
	}
}
