package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.lock.aop.DistributedLockTransaction;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponCommand;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.payment.PaymentCommand;
import kr.hhplus.be.server.domain.payment.PaymentEventPublisher;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.List;


@Slf4j
@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;
	private final ProductService productService;
	private final PointService pointService;
	private final CouponService couponService;
	private final PaymentService paymentService;
	private final OrderEventPublisher orderEventPublisherublisher;

	@Transactional
	public OrderResult.Order order(OrderCriteria.CreateOrder criteria) {
		log.info("주문 시작");
		// 재고 조회
		List<ProductCommand.OrderProduct> orderProducts = criteria.getOrderItems().stream()
			.map(oi -> ProductCommand.OrderProduct.of(
				oi.getProductId(), oi.getQuantity())
			)
			.toList();

		ProductInfo.OrderProducts products = productService.deductOrderItemsStock(ProductCommand.OrderProducts.of(orderProducts));

		// 쿠폰 사용
		Long couponId = null;
		Long discountPrice = 0L;
		if(null != criteria.getCouponId()) {
			Coupon coupon  = couponService.useCoupon(CouponCommand.Use.of(criteria.getUserId(), criteria.getCouponId()));

			couponId = coupon.getId();
			discountPrice = coupon.getDiscountPrice();
		}

		// 주문 생성
		List<OrderCommand.OrderItem> orderItems = products.getOrderProducts().stream().map(op -> OrderCommand.OrderItem.of(
			op.getProductId(),
			op.getProductPrice(),
			op.getQuantity()
		)).toList();

		//주문
		OrderInfo.Order orderInfo = orderService.createOrder(OrderCommand.Create.of(
			criteria.getUserId(),
			couponId,
			discountPrice,
			orderItems)
		);

		pointService.use(PointCommand.Use.of(criteria.getUserId(), orderInfo.getTotalPrice()));

		paymentService.pay(PaymentCommand.Create.of(orderInfo.getId(), orderInfo.getDiscountPrice()));

		orderService.updateStatusToPaid(orderInfo.getId());

		log.info("주문/결제 완료 이벤트발행");
		log.info("->OrderFacade TransactionName:{}", TransactionSynchronizationManager.getCurrentTransactionName());
		log.info("->OrderFacade TransactionActive:{}", TransactionSynchronizationManager.isActualTransactionActive());


		orderEventPublisherublisher.success(criteria.getOrderItems());
		return OrderResult.Order.of(orderInfo.getId(), orderInfo.getTotalPrice(), orderInfo.getDiscountPrice(), orderInfo.getStatus());
	}

	@DistributedLockTransaction(key = "#lockName.concat(':').concat(#criteria.getUserId())")
	public OrderResult.Order orderWithAopDistributedLock(String lockName,OrderCriteria.CreateOrder criteria) {
		// 재고 조회
		List<ProductCommand.OrderProduct> orderProducts = criteria.getOrderItems().stream()
			.map(oi -> ProductCommand.OrderProduct.of(
				oi.getProductId(), oi.getQuantity())
			)
			.toList();

		ProductInfo.OrderProducts products = productService.deductOrderItemsStock(ProductCommand.OrderProducts.of(orderProducts));

		// 쿠폰 사용
		Long couponId = null;
		Long discountPrice = 0L;
		if(null != criteria.getCouponId()) {
			Coupon coupon  = couponService.useCoupon(CouponCommand.Use.of(criteria.getUserId(), criteria.getCouponId()));

			couponId = coupon.getId();
			discountPrice = coupon.getDiscountPrice();
		}

		// 주문 생성
		List<OrderCommand.OrderItem> orderItems = products.getOrderProducts().stream().map(op -> OrderCommand.OrderItem.of(
			op.getProductId(),
			op.getProductPrice(),
			op.getQuantity()
		)).toList();

		//주문
		OrderInfo.Order orderInfo = orderService.createOrder(OrderCommand.Create.of(
			criteria.getUserId(),
			couponId,
			discountPrice,
			orderItems)
		);

		pointService.use(PointCommand.Use.of(criteria.getUserId(), orderInfo.getTotalPrice()));

		paymentService.pay(PaymentCommand.Create.of(orderInfo.getId(), orderInfo.getDiscountPrice()));

		orderService.updateStatusToPaid(orderInfo.getId());
		return OrderResult.Order.of(orderInfo.getId(), orderInfo.getTotalPrice(), orderInfo.getDiscountPrice(), orderInfo.getStatus());
	}
}
