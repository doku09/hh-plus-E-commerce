package kr.hhplus.be.server.interfaces.api.order;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/v1/order")
public interface OrderApi {

	@PostMapping
	ResponseEntity<OrderResponse> order(OrderCreateRequest request);

}
