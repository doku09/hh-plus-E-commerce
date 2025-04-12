package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.common.exception.NotFoundUserException;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	@DisplayName("[실패] 주문시 주문자가 없으면 예외를던진다")
	void order_notFoundUser_exception() {

		// given
		OrderCreateCommand orderCreateCommand = OrderCreateCommand.of(OrderStatus.ORDERED, null, List.of(
				OrderItem.of(Product.create("상품1", 3000), 3),
				OrderItem.of(Product.create("상품2", 5000), 3),
				OrderItem.of(Product.create("상품3", 10000), 3)
			)
		);

		// when

		// then
		assertThatThrownBy(() -> orderService.order(orderCreateCommand))
			.isInstanceOf(NotFoundUserException.class);
	}

	@Test
	@DisplayName("[성공] 주문의 총합은 주문상품의 가격의 총합이다.")
	void orderTotalPrice_equal_orderItemTotal() {

		long userId = 1L;
		String username = "tester";

		// given
		List<OrderItem> orderitems = List.of(
			OrderItem.of(Product.create("상품1", 3000), 3),
			OrderItem.of(Product.create("상품2", 5000), 3),
			OrderItem.of(Product.create("상품3", 10000), 3)
		);

		OrderCreateCommand orderCreateCommand = OrderCreateCommand.of(OrderStatus.ORDERED, User.of(userId, username), orderitems);

		// when
		OrderInfo orderInfo = orderService.order(orderCreateCommand);

		// then
		verify(orderRepository, atLeast(1)).save(any(Order.class));
		assertThat(orderInfo.getTotalPrice()).isEqualTo(orderitems.stream().mapToInt(OrderItem::getTotalPrice)
			.sum());
	}
}
