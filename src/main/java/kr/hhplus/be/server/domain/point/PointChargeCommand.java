package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.interfaces.api.point.request.PointChargeRequest;
import lombok.Builder;

@Builder
public record PointChargeCommand(long amount, long userId, TransactionType type) {

	public static PointChargeCommand toEntity(PointChargeRequest request) {
		return PointChargeCommand
			.builder()
			.amount(request.amount())
			.userId(request.userId())
			.type(TransactionType.CHARGE)
			.build();
	}
}
