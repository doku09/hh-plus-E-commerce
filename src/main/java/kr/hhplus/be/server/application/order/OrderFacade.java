package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.*;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCommand;
import kr.hhplus.be.server.domain.user.UserInfo;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	private final UserService userService;
	private final OrderService orderService;
	private final ProductService productService;

	public OrderResult order(OrderCriteria criteria) {

		List<OrderItem> orderItems = new ArrayList<>();

		// 주문아이템 (상품정보, 개수) 루프 돌면서 저장 후 Order 저장
		for (OrderCriteria.OrderProduct orderProduct : criteria.getOrderProducts()) {
			ProductInfo product = productService.findById(orderProduct.getProductId());

			//주문아이템 생성
			orderItems.add(OrderItem.of(
				Product.of(
					product.getId(),
					product.getName(),
					product.getPrice()),
				orderProduct.getQuantity()
			));
		}

		UserInfo userInfo = userService.findById(criteria.getUserId());

		// TODO QUESTION) order생성할때 user 도메인객체가 필요한데 orderService의 order는 command로 받아야합니다. 이럴때 command안에 user 도메인을 넘겨도 되ㅏ요?
		// orderService안에서 userRepository나 userService를 못쓰니까 여기서..정보를 넘겨주기 위함

		OrderCreateCommand orderCreateCommand = OrderCreateCommand.of(
			OrderStatus.ORDERED,
			User.of(userInfo.id(),userInfo.name()),
			orderItems
		);

		OrderInfo orderInfo = orderService.order(orderCreateCommand);

		return OrderResult.from(orderInfo);
	}
}
