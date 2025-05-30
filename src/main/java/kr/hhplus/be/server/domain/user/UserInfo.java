package kr.hhplus.be.server.domain.user;

import lombok.Getter;

public record UserInfo(long id, String name) {

	@Getter
	public static class User {
		private Long id;
		private String username;

		private User(Long id, String username) {
			this.id = id;
			this.username = username;
		}

		public static User of(Long id, String username) {
			return new User(id, username);
		}
	}
}
