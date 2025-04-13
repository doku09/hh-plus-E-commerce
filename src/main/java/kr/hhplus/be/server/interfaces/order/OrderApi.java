package kr.hhplus.be.server.interfaces.order;


import org.springframework.http.ResponseEntity;

public interface OrderApi {

	ResponseEntity<OrderResponse.Order> order(OrderRequest.CreateOrder request);
}
