package kr.hhplus.be.server.domain.user;


import kr.hhplus.be.server.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {

	private long id;

	private String username;

	public User(long id, String username) {
		this.id = id;
		this.username = username;
	}

	private User(String name) {
		this.username = name;
	}

	public static User create(String username) {
		return new User(username);
	}

	public static User of(long id, String username) {
		return new User(id, username);
	}
}
