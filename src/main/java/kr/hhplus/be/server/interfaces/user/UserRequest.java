package kr.hhplus.be.server.interfaces.user;

import kr.hhplus.be.server.domain.user.UserCommand;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequest {

	@NoArgsConstructor
	@Getter
	public static class Join {
		private String username;

		private Join(String username) {
			this.username = username;
		}

		public static Join of(String username) {
			return new Join(username);
		}

		public UserCommand.Join toCommand() {
			return UserCommand.Join.of(username);
		}
	}
}
