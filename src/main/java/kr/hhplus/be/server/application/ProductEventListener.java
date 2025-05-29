package kr.hhplus.be.server.application;

import kr.hhplus.be.server.application.order.OrderEvent;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class ProductEventListener {

	private final ProductService productService;

	@TransactionalEventListener
	public void onOrderCreated(OrderEvent.Created evt) {

		ProductCommand.OrderProducts orderProducts = ProductCommand.OrderProducts.of(evt.getItems().stream().map(i -> ProductCommand.OrderProduct.of(i.getProductId(), i.getQuantity())).toList());

		productService.deductOrderItemsStock(orderProducts);
	}
}
