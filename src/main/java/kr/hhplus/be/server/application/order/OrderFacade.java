package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStockCommand;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final OrderService orderService;
	private final ProductService productService;

	@Transactional
	public OrderResult.Order order(OrderCriteria.CreateOrder criteria) {
		OrderCommand.Create orderCreateCommand = criteria.toCommand();

		// 주문아이템 (상품정보, 개수) 루프 돌면서 저장 후 Order 저장
		for (OrderCriteria.OrderItem orderProduct : criteria.getOrderItems()) {
			ProductInfo.Product product = productService.findById(orderProduct.getProductId());

			//주문아이템 생성
			orderCreateCommand.addItem(OrderCommand.OrderItem.of(product.getId(),product.getPrice(), orderProduct.getQuantity()));

			//주문 재고 차감 -> 재고 부족 시 주문상태 변경
			try {
				productService.deductStock(ProductStockCommand.Deduct.of(product.getId(), orderProduct.getQuantity()));
			} catch (GlobalBusinessException e) {
				if (e.getMessage().equals(ErrorCode.NOT_ENOUGH_STOCK.getMessage())) {
					//상품 재고에 문제있을 경우 주문 상태 "취소"로 변경
					orderCreateCommand.changeStatus(OrderStatus.CANCELED);
				}
			}
		}

		OrderInfo.Order orderInfo = orderService.order(orderCreateCommand);

		return OrderResult.Order.of(orderInfo.getId(),orderInfo.getTotalPrice(),orderInfo.getStatus());
	}
}
