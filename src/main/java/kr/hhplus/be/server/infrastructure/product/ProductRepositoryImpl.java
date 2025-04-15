package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryImpl implements ProductRepository {

	private final ProductJpaRepository productJpaRepository;
	@Override
	public void save(Product product) {
		productJpaRepository.save(product);
	}

	@Override
	public void saveStock(ProductStock stock) {

	}

	@Override
	public Optional<Product> findById(long id) {
		return Optional.empty();
	}

	@Override
	public Optional<ProductStock> findStockByProductId(long id) {
		return Optional.empty();
	}
}
