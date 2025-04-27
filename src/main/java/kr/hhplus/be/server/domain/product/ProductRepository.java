package kr.hhplus.be.server.domain.product;

import kr.hhplus.be.server.domain.productStock.ProductStock;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
	void save(Product product);

	Optional<Product> findById(long id);

	List<Product> findProductsByIds(List<Long> list);
}
