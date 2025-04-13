package kr.hhplus.be.server.domain.point;

import lombok.Builder;
import lombok.Getter;

@Builder
public class PointInfo {


	@Getter
	public static class Point {

		private Long amount;

		private Point(Long amount) {
			this.amount = amount;
		}

		 public static Point of(Long amount) {
			return new Point(amount);
		 }
	}
}
