package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class ProductServiceTest {

	@InjectMocks
	private ProductService productService;

	@Test
	@DisplayName("[실패] 상품등록 시 상품명을 입력하지 않으면 예외를 던진다.")
	void not_productName_exception() {
		
	  // given
			ProductCreateCommand command = ProductCreateCommand.builder()
				.name("")
				.price(10000)
				.build();

	  // when
			assertThatThrownBy(() -> productService.register(command))
				.isInstanceOf(IllegalArgumentException.class);
	  // then
	}


}