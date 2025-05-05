package kr.hhplus.be.server.application.bestItem;

import kr.hhplus.be.server.domain.bestItem.BestItem;
import kr.hhplus.be.server.domain.bestItem.BestItemService;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Component
public class BestItemScheduler {

	private final OrderService orderService;
	private final ProductService productService;
	private final BestItemService bestItemService;

	@Transactional
	public void saveBestItem() {
		List<OrderItem> orderItems = orderService.getOrderBeforeHour(1);

		Map<Long, Long> salesMap = orderItems.stream().collect(Collectors.groupingBy(
			OrderItem::getProductId,
			Collectors.summingLong(OrderItem::getQuantity)
		));

		salesMap.forEach((productId, salesQuantity) -> {
			// Product 정보를 조회합니다.
			Product product = productService.findById(productId);
			if (product == null) {
				throw new IllegalArgumentException("Product not found for ID: " + productId);
			}

			// BestItem 객체를 생성합니다.
			BestItem findItem = bestItemService.findByProductId(product.getId());
			if(null == findItem) {
				findItem = bestItemService.save(BestItem.create(product, 0L));
			}
			findItem.addSalesCount(salesQuantity);
			bestItemService.save(findItem);
		});
	}
}
