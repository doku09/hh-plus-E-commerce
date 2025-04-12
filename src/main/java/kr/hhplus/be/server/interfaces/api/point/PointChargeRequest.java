package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.domain.point.PointChargeCommand;
import kr.hhplus.be.server.domain.point.TransactionType;

public record PointChargeRequest(long userId, long amount) {

	public static PointChargeCommand toCommand(PointChargeRequest request) {
		return PointChargeCommand
			.builder()
			.amount(request.amount())
			.userId(request.userId())
			.type(TransactionType.CHARGE)
			.build();
	}
}
