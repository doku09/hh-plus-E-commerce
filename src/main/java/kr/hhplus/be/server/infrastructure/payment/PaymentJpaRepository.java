package kr.hhplus.be.server.infrastructure.payment;

import kr.hhplus.be.server.domain.payment.PayStatus;
import kr.hhplus.be.server.domain.payment.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentJpaRepository extends JpaRepository<Payment,Long> {

	@Query("SELECT p FROM Payment p WHERE p.status = :status AND p.createdAt BETWEEN :start AND :end")
	List<Payment> findByStatusAndCreatedAtBetween(@Param("status") PayStatus status,
	                                              @Param("start") LocalDateTime start,
	                                              @Param("end") LocalDateTime end);


}
