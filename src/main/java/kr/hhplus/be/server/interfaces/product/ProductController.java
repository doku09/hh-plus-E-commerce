package kr.hhplus.be.server.interfaces.product;

import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/product")
@RequiredArgsConstructor
@RestController
public class ProductController implements ProductApi{

	private final ProductService productService;


	@Override
	@PostMapping
	public ResponseEntity<Void> register(ProductRequest.Create request) {

		productService.register(request.toCommand());

		return ResponseEntity.ok().build();
	}
}
