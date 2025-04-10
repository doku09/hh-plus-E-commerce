package kr.hhplus.be.server.domain.user;


import kr.hhplus.be.server.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseTimeEntity {

	private long id;

	private String name;

	private User(String name) {
		this.name = name;
	}

	public static User create(String name) {
		return new User(name);
	}

	public static User of(long id, String name) {
		return new User(id, name);
	}
}
