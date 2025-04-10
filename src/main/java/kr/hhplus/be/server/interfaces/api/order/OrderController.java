package kr.hhplus.be.server.interfaces.api.order;

import org.springframework.http.ResponseEntity;

public class OrderController {

	public ResponseEntity<OrderResponse> order(OrderCreateRequest request) {


		return ResponseEntity.ok(new OrderResponse());
	}
}
