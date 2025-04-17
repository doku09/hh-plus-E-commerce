package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderInfo;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.order.PopularOrder;
import kr.hhplus.be.server.domain.payment.PaymentInfo;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.product.ProductInfo;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ProductFacade {

	private final ProductService productService;
	private final OrderService orderService;
	private final PaymentService paymentService;

	@Transactional
	public ProductResult.TopOrderedProducts getTopOrderedProducts(ProductCriteria.TopOrderedProducts criteria) {

		// 결제가 성공된 히스토리 내역을 가져온다.
		PaymentInfo.History paymentHistoryWithPaid = paymentService.getPaymentHistoryWithPaid(criteria.getStartDate(), criteria.getEndDate());

		// 결제 내역을 통해서 주문 내역을 가져온다.
		List<Long> orderIds = paymentHistoryWithPaid.getPayments().stream().map(PaymentInfo.Payment::getOrderId).toList();

		OrderCommand.TopOrderedProducts topOrderedProducts = OrderCommand.TopOrderedProducts.of(orderIds,criteria.getLimitCount());

		// 주문 내역을 통해서 가장 많이 주문된 상품을 가져온다.
		OrderInfo.TopOrder topOrder = orderService.getTopOrder(topOrderedProducts);

		// 상품 정보를 가져온다.
		List<PopularOrder> popularOrders = topOrder.getPopularOrders();
		ProductInfo.TopProducts products = productService.getProductsByIds(popularOrders.stream().map(PopularOrder::getItemId).toList());


		return ProductResult.TopOrderedProducts.of(
			products.getProducts().stream().map(product -> ProductResult.Product.of(
				product.getId(),
				product.getName(),
				product.getPrice()
		)).toList());
	}
}
