package kr.hhplus.be.server.domain.product;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
	void save(Product product);

	void saveStock(ProductStock stock);

	Optional<Product> findById(long id);

	Optional<ProductStock> findStockByProductId(long id);

	List<Product> findProductsByIds(List<Long> list);
}
