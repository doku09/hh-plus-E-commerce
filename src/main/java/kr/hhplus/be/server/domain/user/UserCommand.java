package kr.hhplus.be.server.domain.user;

import lombok.Getter;

@Getter
public class UserCommand {

	private long id;

	private String name;

	private UserCommand(long id, String name) {
		this.id = id;
		this.name = name;
	}

	public User toEntity() {
		return User.builder()
			.name(this.name)
			.build();
	}

	public static UserCommand of(long id, String name) {
		return new UserCommand(id,name);
	}
}
