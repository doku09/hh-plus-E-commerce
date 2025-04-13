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

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

	@InjectMocks
	private ProductService productService;
	@Mock
	private ProductRepository productRepository;

	@Test
	@DisplayName("[실패] 상품등록 시 상품명을 입력하지 않으면 예외를 던진다.")
	void not_productName_exception() {
		
	  // given
		ProductCommand.Create command = ProductCommand.Create.of(" ", 1000);

		// when & then
			assertThatThrownBy(() -> productService.register(command))
				.isInstanceOf(GlobalBusinessException.class)
				.hasMessage(ErrorCode.NOT_EMPTY_PRICE_NAME.getMessage());
	}
	
	@ParameterizedTest
	@DisplayName("[실패] 상품등록시 가격을 음수로 하면 예외가발생한다.")
	@ValueSource(ints = {-1000, -1, -100})
	void registerProduct_negativePrice_throwException(int price) {
		
	  // given
		ProductCommand.Create command = ProductCommand.Create.of("테스트", price);

	  // when & then
		assertThatThrownBy(() -> productService.register(command))
			.isInstanceOf(NegativePriceException.class);
	}

	@ParameterizedTest
	@DisplayName("[성공] 상품 이름과 가격을 입력하여 상품을 등록한다")
	@CsvSource(value = {"당근:0", "사과:1", "포도:1000"}, delimiter = ':') // 기본은 ,
	void registerProduct_success(String name,int price) {

	  // given
		ProductCommand.Create command = ProductCommand.Create.of(name, price);

		// when
		productService.register(command);

	  // then
		verify(productRepository,atLeast(1)).save(any(Product.class));
		verify(productRepository,atLeast(1)).saveStock(any(ProductStock.class));
	}


}