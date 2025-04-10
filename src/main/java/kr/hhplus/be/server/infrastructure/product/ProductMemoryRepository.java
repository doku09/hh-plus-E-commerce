package kr.hhplus.be.server.infrastructure.product;


import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStock;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class ProductMemoryRepository implements ProductRepository {

	public Map<Long, Product> productTable = new HashMap<>();
	private long sequence = 0;

	@Override
	public void save(Product product) {
		product.setId(sequence);
		productTable.put(sequence,product);
	}

	@Override
	public void saveStock(ProductStock stock) {
		stock.setId(sequence);
	}

	@Override
	public Optional<Product> findById(long id) {
		return Optional.of(productTable.get(id));
	}

	@Override
	public Optional<List<Product>> findByIds(List<String> productIds) {
		return Optional.empty();
	}

	@Override
	public Optional<ProductStock> findStockByProductId(long id) {
		return Optional.empty();
	}
}
