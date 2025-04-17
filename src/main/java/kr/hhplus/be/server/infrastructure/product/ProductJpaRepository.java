package kr.hhplus.be.server.infrastructure.product;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductJpaRepository extends JpaRepository<Product, Long> {

	@Modifying
	@Query(value = "INSERT INTO product_stock (product_id, stock) VALUES (:productId, :stock)", nativeQuery = true)
	void saveStock(ProductStock stock);

	@Query(value = "SELECT * FROM product_stock WHERE product_id = :productId", nativeQuery = true)
	Optional<ProductStock> findStockByProductId(@Param("productId") Long productId);


	@Query("SELECT p FROM Product p WHERE p.id IN :ids")
	List<Product> findAllByIds(@Param("ids") List<Long> ids);
}
