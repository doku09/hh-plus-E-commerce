package kr.hhplus.be.server.infrastructure.stock;

import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.domain.productStock.ProductStock;
import kr.hhplus.be.server.domain.productStock.ProductStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductStockRepositoryImpl implements ProductStockRepository {

	private final ProductStockJpaRepository jpaRepository;

	@Override
	public ProductStock save(ProductStock stock) {
		return jpaRepository.save(stock);
	}

	@Override
	public ProductStock findById(Long id) {
		return jpaRepository.findById(id).orElseThrow(()->new GlobalBusinessException(ErrorCode.NOT_FOUND_STOCK));
	}

	@Override
	public ProductStock findByProductId(Long productId) {
		return jpaRepository.findByProductId(productId);
	}
}
