package kr.hhplus.be.server.domain.user;

public record UserInfo(long id, String name) {

	public static UserInfo from(User user) {
		return new UserInfo(user.getId(),user.getName());
	}
}
