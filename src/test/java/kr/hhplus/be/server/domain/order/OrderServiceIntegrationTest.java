package kr.hhplus.be.server.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
public class OrderServiceIntegrationTest {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderService orderService;

	@Test
	@DisplayName("[성공] 주문을 생성한다.")
	@Transactional
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
	@Transactional
	void get_top_order_items() {

		// given
		int limit = 2;
		OrderCommand.TopOrderedProducts topOrderedProducts = OrderCommand.TopOrderedProducts.of(List.of(1L, 2L, 3L, 4L), limit);

		// when
		Order order1 = Order.createOrder(1L);
		order1.addItem(OrderItem.of(22L, 1000L, 5));
		order1.addItem(OrderItem.of(42L, 1000L, 5));
		order1.addItem(OrderItem.of(67L, 1000L, 5));

		Order order2 = Order.createOrder(2L);
		order2.addItem(OrderItem.of(34L, 1000L, 5));
		order2.addItem(OrderItem.of(34L, 1000L, 5));
		order2.addItem(OrderItem.of(34L, 1000L, 5));

		Order order3 = Order.createOrder(3L);
		order3.addItem(OrderItem.of(22L, 1000L, 5));
		order3.addItem(OrderItem.of(22L, 1000L, 5));
		order3.addItem(OrderItem.of(22L, 1000L, 5));


		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);

		OrderInfo.TopOrder topOrder =
			orderService.getTopOrder(topOrderedProducts);

		// then
		assertThat(topOrder).isNotNull();
		assertThat(topOrder.getPopularOrders()).isNotNull();
		assertThat(topOrder.getPopularOrders().size()).isEqualTo(2);
		assertThat(topOrder.getPopularOrders().get(0).getItemId()).isEqualTo(22L);
		assertThat(topOrder.getPopularOrders().get(1).getItemId()).isEqualTo(34L);
	}
}
