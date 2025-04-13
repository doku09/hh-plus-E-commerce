package kr.hhplus.be.server.domain.point;

import lombok.Getter;

public class PointCommand {

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
	}
	@Getter
	public static class Use {
		private Long userId;
		private Long amount;

		private Use(Long userId, Long amount) {
			this.userId = userId;
			this.amount = amount;
		}

		public static Use of(Long userId, Long amount) {
			return new Use(userId, amount);
		}
	}
}
