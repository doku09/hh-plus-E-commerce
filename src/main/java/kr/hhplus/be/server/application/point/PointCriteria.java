package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointCommand;
import lombok.Getter;

public class PointCriteria {

	@Getter
	public static class Charge {
		private Long userId;
		private Long amount;

		private Charge(Long userId, Long amount) {
			this.userId = userId;
			this.amount = amount;
		}

		public static Charge of(Long userId, Long amount) {
			return new Charge(userId, amount);
		}

		public PointCommand.Charge toCommand() {
			return PointCommand.Charge.of(userId, amount);
		}
	}
}
