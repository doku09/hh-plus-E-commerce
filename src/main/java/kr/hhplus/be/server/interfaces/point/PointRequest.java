package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.domain.point.PointCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class PointRequest {

	@Getter
	@NoArgsConstructor
	public static class Charge {
		private Long amount;

		private Charge(Long amount) {
			this.amount = amount;
		}

		public static Charge of(Long amount) {
			return new Charge(amount);
		}

		public PointCommand.Charge toCommand(Long userId) {
			return PointCommand.Charge.of(userId, amount);
		}
	}
}
