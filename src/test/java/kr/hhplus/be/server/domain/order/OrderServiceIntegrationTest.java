package kr.hhplus.be.server.domain.order;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
	@DisplayName("인기상품 조회를 위해 1시간전 주문된 상품을 가져온다.")
	void getOrderBeforeFiveMiniutes() {

	  // given
		Order order1 = OrderFixture.createOrderWithOrderItems(1L,OrderStatus.PAID);
		Order order2 = OrderFixture.createOrderWithOrderItems(2L,OrderStatus.PAID);
		Order order3 = OrderFixture.createOrderWithOrderItems(3L,OrderStatus.PAID);

		orderRepository.save(order1);
		orderRepository.save(order2);
		orderRepository.save(order3);
	  // when
		List<OrderItem> orders = orderService.getOrderBeforeHour(1);
		// then
		assertThat(orders.size()).isGreaterThan(1);
	}
}
