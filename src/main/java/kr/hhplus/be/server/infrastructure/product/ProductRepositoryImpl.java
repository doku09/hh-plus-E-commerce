package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStock;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
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
		productJpaRepository.saveStock(stock);
	}

	@Override
	public Optional<Product> findById(long id) {
		return productJpaRepository.findById(id);
	}

	@Override
	public Optional<ProductStock> findStockByProductId(long id) {
		return productJpaRepository.findStockByProductId(id);
	}

	@Override
	public List<Product> findProductsByIds(List<Long> list) {
		return productJpaRepository.findAllByIds(list);
	}
}
