package kr.hhplus.be.server.domain.point;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import kr.hhplus.be.server.common.exception.MaxPointException;
import kr.hhplus.be.server.common.exception.NotEnoughPointException;
import kr.hhplus.be.server.domain.user.User;
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

	private Long userId;

	public void charge(Long amount) {
		if(this.amount + amount > MAX_POINT) {
			throw new MaxPointException();
		}

		this.amount = this.amount + amount;
	}

	// TODO QUESTION) 더티체킹을 도메인 클래스에서 하는게 맞을까요?
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
