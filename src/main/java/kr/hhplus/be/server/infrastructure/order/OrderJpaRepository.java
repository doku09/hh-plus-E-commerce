package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

	@Query("SELECT oi FROM OrderItem oi WHERE oi.order.id IN :orderIds")
	List<OrderItem> findAllOrderItemsByIds(@Param("orderIds") List<Long> orderIds);
}
