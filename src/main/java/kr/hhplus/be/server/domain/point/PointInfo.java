package kr.hhplus.be.server.domain.point;

import lombok.Builder;

@Builder
public record PointInfo(long id, long amount, long userId) {

	public static PointInfo from(Point point) {

		// TODO QUESTION of from toEntity 등 팩토리메서드 어떻게 사용해야 적절한지
		// TODO QUESTION point 안에 User 응답객체도 응답 DTO로 변환해야하는지..
		return PointInfo.builder()
			.id(point.getId())
			.userId(point.getUser().getId())
			.amount(point.getAmount())
			.build();
	}
}
