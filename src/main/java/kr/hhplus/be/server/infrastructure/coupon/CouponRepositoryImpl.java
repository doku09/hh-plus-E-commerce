package kr.hhplus.be.server.infrastructure.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {

	private CouponJpaRepository couponJpaRepository;

	@Override
	public Coupon saveCoupon(Coupon coupon) {
		return couponJpaRepository.save(coupon);
	}

	@Override
	public Optional<Coupon> findCouponById(long id) {
		return Optional.empty();
	}

	@Override
	public void issueCoupon(UserCoupon userCoupon) {

	}

	@Override
	public List<UserCoupon> findIssuedCouponByUserId(long id) {
		return List.of();
	}

	@Override
	public UserCoupon findIssuedCouponByUserIdAndCouponId(Long userId, Long couponId) {
		return null;
	}
}
