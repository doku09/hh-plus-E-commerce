package kr.hhplus.be.server.domain.productStock;


public interface ProductStockRepository {

	ProductStock save(ProductStock stock);

	ProductStock findById(Long id);

	ProductStock findByProductId(Long productId);
}
