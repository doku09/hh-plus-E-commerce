package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController implements OrderApi{

	private final OrderFacade orderFacade;

	@Override
	@PostMapping
	public ResponseEntity<OrderResponse.Order> order(@RequestBody OrderRequest.CreateOrder request) {
		OrderResult.Order ordered = orderFacade.order(request.toCriteria());
		return ResponseEntity.ok(OrderResponse.Order.of(ordered.getId(),ordered.getTotalPrice(),ordered.getOrderStatus()));
	}
}
