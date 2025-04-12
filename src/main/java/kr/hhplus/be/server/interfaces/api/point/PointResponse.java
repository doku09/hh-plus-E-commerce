package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.domain.point.Point;

public record PointResponse(long id, long amount, long userId) {
	public static PointResponse from(Point point) {
		return new PointResponse(
			point.getId(),
			point.getAmount(),
			point.getUser().getId()
		);
	}
}
