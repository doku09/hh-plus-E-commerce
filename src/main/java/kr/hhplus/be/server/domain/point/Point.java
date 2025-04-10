package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.common.exception.MaxPointException;
import kr.hhplus.be.server.common.exception.NotEnoughPointException;
import kr.hhplus.be.server.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class Point {

	public static final Long MAX_POINT = 10_000_000L;
	public static final Long ZERO_POINT = 0L;

	private long id;

	private long amount;

	private User user;

	public void charge(long amount) {
		if(this.amount + amount > MAX_POINT) {
			throw new MaxPointException();
		}

		this.amount = this.amount + amount;
	}

	// TODO QUESTION) 더티체킹을 도메인 클래스에서 하는게 맞을까요?
	public void use(long amount) {
		if(this.amount - amount < 0) {
			throw new NotEnoughPointException();
		}

		this.amount = this.amount - amount;
	}
}
