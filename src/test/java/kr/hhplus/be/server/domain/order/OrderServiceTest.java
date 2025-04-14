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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	@DisplayName("[성공] 주문의 총합은 주문상품의 가격의 총합이다.")
	void orderTotalPrice_equal_orderItemTotal() {

		// given
		long userId = 1L;

		Order order = Order.createOrder(userId);

		// 세가지 종류의 아이템을 추가. 총합: 12000원
		order.addItem(OrderItem.of(1L,3000,2));
		order.addItem(OrderItem.of(2L,2000,2));
		order.addItem(OrderItem.of(3L,1000,2));

		// when
		OrderInfo.Order result = orderService.order(order);

		// then
		verify(orderRepository,times(1)).save(any());

		assertThat(result.getTotalPrice()).isEqualTo(12000);
	}
}
