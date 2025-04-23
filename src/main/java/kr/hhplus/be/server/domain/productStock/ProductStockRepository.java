package kr.hhplus.be.server.domain.productStock;

import java.util.Optional;

public interface ProductStockRepository {

	ProductStock save(ProductStock stock);

	ProductStock findById(Long id);

	ProductStock findByProductId(Long productId);
}
