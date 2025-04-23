package kr.hhplus.be.server.domain.user;


import jakarta.persistence.*;
import kr.hhplus.be.server.domain.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name="users")
public class User extends BaseTimeEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
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
