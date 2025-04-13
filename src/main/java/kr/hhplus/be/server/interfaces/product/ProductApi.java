package kr.hhplus.be.server.interfaces.product;

import org.springframework.http.ResponseEntity;

public interface ProductApi {

	public ResponseEntity<Void> register(ProductRequest.Create request);

}
