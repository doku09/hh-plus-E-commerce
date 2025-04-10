package kr.hhplus.be.server.interfaces.api.user.request;

import kr.hhplus.be.server.domain.user.UserJoinCommand;

public record UserJoinRequest(String name) {

	public UserJoinCommand toCommand() {
		return UserJoinCommand.builder()
			.name(name)
			.build();
	}
}
