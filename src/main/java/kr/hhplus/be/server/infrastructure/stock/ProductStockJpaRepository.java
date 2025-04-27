package kr.hhplus.be.server.infrastructure.stock;

import kr.hhplus.be.server.domain.productStock.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductStockJpaRepository extends JpaRepository<ProductStock, Long> {
	ProductStock findByProductId(long id);
}
