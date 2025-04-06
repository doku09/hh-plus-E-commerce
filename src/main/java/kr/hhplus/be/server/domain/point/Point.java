package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.domain.user.User;
import lombok.Getter;

@Getter
public class Point {

	public static final Long MAX_POINT = 10_000_000L;

	private long id;

	private long amount;

	private User user;
}
