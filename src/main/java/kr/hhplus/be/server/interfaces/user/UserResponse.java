package kr.hhplus.be.server.interfaces.user;

public class UserResponse {

	public static class User {
		private Long id;
		private String username;

		public User(Long id, String username) {
			this.id = id;
			this.username = username;
		}

		public static User of(Long id, String username) {
			return new User(id, username);
		}
	}
}
