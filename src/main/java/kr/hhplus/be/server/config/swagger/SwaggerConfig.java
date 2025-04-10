package kr.hhplus.be.server.config.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
	info = @io.swagger.v3.oas.annotations.info.Info(
		title = "Mock API 문서",
		description = "상품,주문,결제,쿠폰 관련 API 명세",
		version = "v1.0"
	)
)
public class SwaggerConfig {
	//http://localhost:8080/swagger-ui/index.html

	/**
	 * components() 의 경우, 공통적으로 처리해야하는 Http Header, 스키마, 응답, 파라미터 등과 같은 공통요소를 정의하는 역할을 하며
	 * API 문서를 재사용 가능하게 만들어 효율적으로 처리할 수 있다.
	 *
	 * @return
	 */
	@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components()
				.addSecuritySchemes("BearerAuth", new SecurityScheme()
					.type(SecurityScheme.Type.HTTP)
					.scheme("bearer")
					.bearerFormat("JWT"))
			)
			.info(info());
	}

	private Info info() {
		return new Info()
			.title("HH-Plus-Ecommerce API")
			.description("이커머스 API 스펙")
			.version("1.0");
	}
}
