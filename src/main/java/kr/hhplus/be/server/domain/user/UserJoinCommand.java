package kr.hhplus.be.server.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class UserJoinCommand {

	private long id;

	private String name;

	public User toEntity() {
		return User.builder()
			.name(this.name)
			.build();
	}
}
