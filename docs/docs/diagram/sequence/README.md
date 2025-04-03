## 시퀀스 다이어그램 

> **고민한 부분**
### 1. 주문과 결제는 주문을 함과 동시에 이루어지나요?
- 만약 A상품의 재고는 10개라고 가정했을때 
- 홍길동이라는 사람이 A상품 8개를 먼저 선택하여 주문 페이지에 들어왔고 
- 김홍식이라는 사람이 늦게 주문페이지에 들어왔지만 A상품 5개를 홍길동보다 빨리 결제를 시작했을때 김홍식이는 결제를 할 수 있나요? 상품에 대한 우선권이 누구에게 있는지 궁금합니다.
```
다른 예시 - 한국사 시험
- A학생이 파랑초등학교에서 치루게되는 한국사 시험을 온라인으로 신청하려한다.
- 파랑초등학교에 신청가능한 자리수는 한자리다. 
 - A학생은 자리를 먼저 선택하였다.
 - B학생은 A학생보다 늦게 자리를 선택했지만 결제를 더 빨리했다. 
 
 - 누구에게 시험 자리를 줘야하는지 궁금합니다.
```

### 2. 도메인을 객체로하여 다이어그램을 그리다보니 어려움을 겪었습니다.
- 도메인에게 책임을 할당하는것이 어렵게 느껴집니다.
- 예를 들어 쿠폰 사용 가능 여부를 확인하는 책임은 Coupon이 가져야 하는건지 Order가 가져야하는 것인지  궁금합니다.
- OrderService 레이어에서 쿠폰 사용여부를 가져올 것 같은데 이럴경우 그림처럼 Order에서 Coupon으로 향하게 하는 것이 맞는지 
- 서비스 레이어에서 실행되어야할 로직은 도메인 시퀀스 다이어그램에서 어떻게 표시해야하는지 모르겠습니다.


```mermaid
sequenceDiagram
    participant A as Client
    participant PO as Point
    participant O as Order
    participant OI as OrderItem
    participant P as Product
    participant C as Coupon
    participant PAY as Payment
    participant CM as CouponMemberAssociations
    participant CU as CouponUsage
    participant CAL as PriceCalculator
    
    %% 전제
    Note right of O: 주문보다 결제를 먼저 한사람이 상품을 갖는다.

    A->>OI:주문상품 선택
    A->>+O:주문시도
    O->>+P:재고확인
    P->>-O:재고응답
    alt 재고없음
    P--xA:재고없음예외
    end
    O->>-A:주문 정상 처리

    O->>+C:사용가능한 쿠폰인지 확인
    C->>O:쿠폰 사용가능여부 응답

    alt 쿠폰사용가능
C->>CU:어떤 사용자가 어떤쿠폰을 사용했는지 확정한다
    else 쿠폰사용불가
    C--xO:쿠폰이 유효하지 않음을 반환
    end

    A->>+PAY:결제시도

    O->>+P:재고확인
    P->>-O:재고응답

    alt 재고없음
    P--xA:재고없음예외
    end

    PAY->>CAL:최종결제금액 계산
    CAL->>PAY:최종결제금액 응답

    O->>PO:포인트 조회
    PO->>O:포인트 잔액 응답

    PAY->>-A:결제 성공 여부 반환
    
```
![주문&결제 시퀀스.png](%EC%A3%BC%EB%AC%B8%26%EA%B2%B0%EC%A0%9C%20%EC%8B%9C%ED%80%80%EC%8A%A4.png)