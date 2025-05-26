# MSA(Microservice Architecture)로 전환하기
이번 과제에서는 모놀리식 구조에서 분리·독립 배포가 가능한 MSA(Microservice Architecture)로 전환하는 과정을 다룹니다.  
MSA는 작은 단위의 서비스들이 각각 독립적으로 개발·배포되는 아키텍처로, “주문·재고·쿠폰” 같은 도메인을 하나의 서버가 아닌 개별 서비스로 분리하여 운영합니다.

그렇다면, 왜 MSA를 선택해야 할까?
기존에 많이 쓰이던 모놀리식 아키텍처의 단점을 살펴보면 답이 나옵니다.

## 모놀리식 아키텍처의 단점
- 부분장애가 전체 서비스의 장애로 이어질 수 있다.
- 전체 시스템에 대한 구조 파악이 어렵다.
- 서비스가 커질수록 복잡해지고 유지보수 및 확장이 어렵다.

> 이러한 한계를 극복하기 위해 MSA는 관심사를 분리하여 유지보수를 보다 수월하게 하며, 각 서비스가 독립적으로 배포되고 확장 가능하도록 해줍니다.

## 도메인 설계

서비스명 | 주요 책임
-- | --
Order Service| 주문 생성, 조회, 상태 변경
Product Service|상품 정보 및 관리/재고 조회, 재고차감
Coupon Service|쿠폰 발급·사용
Payment Service|결제 승인
Point Service|포인트 적립·차감

### 트랜잭션 분산환경의 문제점과 해결방안

#### 문제점
단일 DB 트랜잭션이 서비스 경계를 넘지 못해, 부분 실패 시 전체 롤백이 불가능합니다.

#### 해결방안 (Saga Pattern)

- Orchestration: 중앙 오케스트레이터(Service)가 단계별 커맨드를 발행하고, 실패 시 역순으로 보상 트랜잭션(compensation)을 실행합니다.

- Choreography: 주문 생성 → 재고 차감 → 쿠폰 적용 → 결제 완료 순으로 이벤트를 체이닝하며, 각 서비스는 자신의 이벤트를 발행·구독해 자율 처리합니다.

## 코드구현 - Coreography

### AS-IS

```java
class OrderFacade{
	@Transactional
	public OrderResult.Order order(OrderCriteria.CreateOrder criteria) {
		// 재고 차감
		ProductInfo.OrderProducts products = productService.deductOrderItemsStock(ProductCommand.OrderProducts.of(orderProducts));
		// 쿠폰 사용
        Coupon coupon  = couponService.useCoupon(CouponCommand.Use.of(criteria.getUserId(), criteria.getCouponId()));
		// 포인트 사용
		pointService.use(PointCommand.Use.of(criteria.getUserId(), orderInfo.getTotalPrice()));
		// 결제
		paymentService.pay(PaymentCommand.Create.of(orderInfo.getId(), orderInfo.getDiscountPrice()));
		// 주문 상태 변경
		orderService.updateStatusToPaid(orderInfo.getId());
        //이벤트 발행
		orderEventPublisherublisher.success(criteria.getOrderItems());
		return OrderResult.Order.of(orderInfo.getId(), orderInfo.getTotalPrice(), orderInfo.getDiscountPrice(), orderInfo.getStatus());
	}
]
```
### TO-BE
OrderFacade를 사용하지 않고 각 도메인 서비스에서 서비스를 호출합니다.
주문요청 -> 주문결제완료 -> (주문결제완료 이벤트발행) -> (재고차감 리스너) -> 재고차감

OrderService
```java
public OrderInfo.Order createOrder(OrderCommand.Create command) {

    Order order = Order.createOrder(command.getUserId());
    order.applyCoupon(command.getCouponId(), command.getDiscountPrice());
    // 주문 저장
    orderRepository.save(order);

    // 주문완료 이벤트 발행
  try {
	  eventPublisher.publish(new OrderEvent.Created(
		  order.getId(),
		  command.getUserId(),
		  order.getOrderItems().stream()
			  .map(oi -> new OrderItemDto(oi.getId(), oi.getProductId(), oi.getQuantity(), oi.getProductPrice(), oi.getTotalPrice()))
			  .toList()
	  ));
  } catch() {
		보상 트랜잭션();
  }

    return OrderInfo.Order.of(order.getId(), order.getTotalPrice(), order.getDiscountPrice(), order.getStatus());
}
```

```java
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
```

각 도메인은 자신의 역할만 인지하고, 주문 서비스는 오직 ‘주문 생성’에만 집중합니다. 
주문이 완료되면 이벤트를 발행해 재고·쿠폰·결제 등 각 서비스의 리스너가 순차적으로 실행되며, 처리 중 오류가 발생할 경우 미리 정의된 보상 트랜잭션을 통해 자동으로 복구됩니다.
