package kr.hhplus.be.server.domain.point;

import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.MaxPointException;
import kr.hhplus.be.server.common.exception.NotEnoughPointException;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Entity
public class Point {

	public static final Long MAX_POINT = 10_000_000L;
	public static final Long ZERO_POINT = 0L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private Long amount;

	@Column(nullable = false, updatable = false)
	private Long userId;

	@Version
	private Long version;

	public void charge(Long amount) {
		if(this.amount + amount > MAX_POINT) {
			throw new MaxPointException();
		}

		this.amount = this.amount + amount;
	}

	public void use(Long amount) {
		if(this.amount - amount < 0) {
			throw new NotEnoughPointException();
		}

		this.amount = this.amount - amount;
	}

	// 생성자
	private Point(Long amount, Long userId) {
		this.amount = amount;
		this.userId = userId;
	}

	public static Point of(Long amount, Long userId) {
		return new Point(amount, userId);
	}
}
