package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductStock;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final UserService userService;
	private final OrderService orderService;
	private final ProductService productService;

	@Transactional
	public OrderResult order(OrderCriteria criteria) {

		UserInfo userInfo = userService.findById(criteria.getUserId());

		OrderCreateCommand orderCreateCommand = OrderCreateCommand.of(
			OrderStatus.ORDERED,
			User.of(userInfo.id(),userInfo.name())
		);

		// 주문아이템 (상품정보, 개수) 루프 돌면서 저장 후 Order 저장
		for (OrderCriteria.OrderProduct orderProduct : criteria.getOrderProducts()) {
			ProductInfo product = productService.findById(orderProduct.getProductId());

			//주문아이템 생성
			orderCreateCommand.addItem(OrderItem.of(
				Product.of(
					product.getId(),
					product.getName(),
					product.getPrice()),
				orderProduct.getQuantity()
			));

			ProductStock stock = productService.findStockByProductId(product.getId());

			try {
				stock.deduct(orderProduct.getQuantity());
			} catch (GlobalBusinessException e) {
				if(e.getMessage().equals(ErrorCode.NOT_ENOUGH_STOCK.getMessage())) {
					//상품 재고에 문제있을 경우 주문 상태 "취소"로 변경
					orderCreateCommand.changeStatus(OrderStatus.CANCELED);
				}
			}
		}

		// TODO QUESTION) order생성할때 user 도메인객체가 필요한데 orderService의 order는 command로 받아야합니다. 이럴때 command안에 user 도메인을 넘겨도 되ㅏ요?
		// orderService안에서 userRepository나 userService를 못쓰니까 여기서..정보를 넘겨주기 위함
		OrderInfo orderInfo = orderService.order(orderCreateCommand);

		return OrderResult.from(orderInfo);
	}
}
