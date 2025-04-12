package kr.hhplus.be.server.domain.point;

import lombok.Builder;

@Builder
public record PointUseCommand(long amount,long userId) {
}
