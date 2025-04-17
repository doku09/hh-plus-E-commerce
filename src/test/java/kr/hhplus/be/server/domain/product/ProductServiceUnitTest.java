package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.common.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceUnitTest {

	@InjectMocks
	private ProductService productService;
	@Mock
	private ProductRepository productRepository;

	@Test
	@DisplayName("[실패] 상품등록 시 상품명을 입력하지 않으면 예외를 던진다.")
	void not_productName_exception() {
		
	  // given
		ProductCommand.Create command = ProductCommand.Create.of(" ", 1000L,0);

		// when & then
			assertThatThrownBy(() -> productService.register(command))
				.isInstanceOf(GlobalBusinessException.class)
				.hasMessage(ErrorCode.NOT_EMPTY_PRICE_NAME.getMessage());
	}
	
	@ParameterizedTest
	@DisplayName("[실패] 상품등록시 가격을 음수로 하면 예외가발생한다.")
	@ValueSource(longs = {-1000, -1, -100})
	void registerProduct_negativePrice_throwException(Long price) {
		
	  // given
		ProductCommand.Create command = ProductCommand.Create.of("테스트", price,0);

	  // when & then
		assertThatThrownBy(() -> productService.register(command))
			.isInstanceOf(NegativePriceException.class);
	}

	@ParameterizedTest
	@DisplayName("[성공] 상품 이름과 가격을 입력하여 상품을 등록한다")
	@CsvSource(value = {"당근:0", "사과:1", "포도:1000"}, delimiter = ':') // 기본은 ,
	void registerProduct_success(String name,Long price) {

	  // given
		ProductCommand.Create command = ProductCommand.Create.of(name, price,0);

		// when
		productService.register(command);

	  // then
		verify(productRepository,atLeast(1)).save(any(Product.class));
		verify(productRepository,atLeast(1)).saveStock(any(ProductStock.class));
	}

	@ParameterizedTest
	@ValueSource(ints = {10, 7, 6})
	@DisplayName("[실패] 주문 시 주문상품의 재고가 부족하면 예외를 던진다")
	void notEnoughStock_exception(int quantity) {

	  // given
		List<ProductCommand.OrderProduct> orderProducts = List.of(
			ProductCommand.OrderProduct.of(1L, quantity),
			ProductCommand.OrderProduct.of(2L, quantity)
			);

		Product product1 = new Product(1L, "커피", 3000L);

		// 두개다 5개씩밖에없음
		ProductStock stock1 = new ProductStock(product1,5); // 수량 10개

		ProductCommand.OrderProducts command = ProductCommand.OrderProducts.of(orderProducts);

		// when
		when(productRepository.findStockByProductId(1L)).thenReturn(Optional.of(stock1));

		// then
		assertThatThrownBy(() -> productService.deductStock(command))
			.isInstanceOf(GlobalBusinessException.class)
			.hasMessage(ErrorCode.NOT_ENOUGH_STOCK.getMessage());
	}

	@Test
	@DisplayName("[성공] 주문 성공시 재고차감")
	void success_order_deductStock() {

	  // given

		// 1번 1개, 2번 1개
		ProductCommand.OrderProducts orderProducts = ProductCommand.OrderProducts.of(List.of(
			ProductCommand.OrderProduct.of(1L, 1),
			ProductCommand.OrderProduct.of(2L, 1))
		);

		// when
		Product product1 = new Product(1L, "커피", 3000L);
		Product product2 = new Product(2L, "커피", 3000L);

		ProductStock stock1 = new ProductStock(product1,5);
		ProductStock stock2 = new ProductStock(product2,5);

		// 1번 2번 5개씩 존재
		when(productRepository.findStockByProductId(1L)).thenReturn(Optional.of(stock1));
		when(productRepository.findStockByProductId(2L)).thenReturn(Optional.of(stock2));

		//
		when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
		when(productRepository.findById(2L)).thenReturn(Optional.of(product2));
		ProductInfo.OrderProducts products = productService.deductStock(orderProducts);


	  // then
		assertThat(products.getOrderProducts().get(0).getQuantity()).isEqualTo(4);
		assertThat(products.getOrderProducts().get(1).getQuantity()).isEqualTo(4);
	}
}