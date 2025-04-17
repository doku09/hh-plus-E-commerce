package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {
	List<UserCoupon> findByUserId(long userId);

	Optional<UserCoupon> findByUserIdAndCouponId(Long userId, Long couponId);
}
