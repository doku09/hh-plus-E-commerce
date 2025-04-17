package kr.hhplus.be.server.domain.order;

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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceUnitTest {

	@Mock
	private OrderRepository orderRepository;

	@InjectMocks
	private OrderService orderService;

	@Test
	@DisplayName("[성공] 주문의 총합은 주문상품의 가격의 총합이다.")
	void orderTotalPrice_equal_orderItemTotal() {

		// given
		long userId = 1L;
		long couponId = 1L;
		long discountPrice = 1000L;
		List<OrderCommand.OrderItem> orderItems = List.of(
			OrderCommand.OrderItem.of(1L, 1000L, 1),
			OrderCommand.OrderItem.of(2L, 2000L, 2)
		);

		// when
		OrderCommand.Create create = OrderCommand.Create.of(userId, couponId, discountPrice, orderItems);

		OrderInfo.Order order = orderService.createOrder(create);

		// then
		assertThat(order).isNotNull();
		assertThat(order.getTotalPrice()).isEqualTo(5000L);
	}

	@Test
	@DisplayName("[성공] 주문수량이 많은 순서대로 상품을 가져온다")
	void get_top_order_items() {

	  // given
		int limit = 2;
		OrderCommand.TopOrderedProducts topOrderedProducts = OrderCommand.TopOrderedProducts.of(List.of(1L, 2L, 3L, 4L), limit);

	  // when
		when(orderRepository.findAllOrderItemsByIds(any()))
		  .thenReturn(List.of(
			  OrderItem.of(1L, 1000L, 5),
			  OrderItem.of(2L, 2000L, 7),
			  OrderItem.of(3L, 3000L, 8),
			  OrderItem.of(4L, 4000L, 4)
		  ));

	  OrderInfo.TopOrder topOrder =
		orderService.getTopOrder(topOrderedProducts);

	  // then
		assertThat(topOrder).isNotNull();
		assertThat(topOrder.getPopularOrders()).isNotNull();
		assertThat(topOrder.getPopularOrders().size()).isEqualTo(2);
		assertThat(topOrder.getPopularOrders().get(0).getItemId()).isEqualTo(3L);
		assertThat(topOrder.getPopularOrders().get(1).getItemId()).isEqualTo(2L);
	}

}
