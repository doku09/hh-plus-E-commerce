package kr.hhplus.be.server.domain.user;

import lombok.Getter;

@Getter
public class UserCommand {

	@Getter
	public static class Join {
		private String username;

		private Join(String username) {
			this.username = username;
		}

		public static Join of(String username) {
			return new Join(username);
		}
	}
}
