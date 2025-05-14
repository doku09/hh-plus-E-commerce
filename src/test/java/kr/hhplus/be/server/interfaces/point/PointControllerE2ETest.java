package kr.hhplus.be.server.interfaces.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@ActiveProfiles("test")
@SpringBootTest
public class PointControllerE2ETest {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PointRepository pointRepository;

	private User user;

	@BeforeEach
	void setUp() {
		user = User.create("동동");
		userRepository.save(user);
	}

	@Test
	@DisplayName("[성공] 잔액을 조회한다.")
	void getPoint() {

		// given
		Point point = Point.of(10000L,user.getId());
		point.charge(100_000L);
		pointRepository.save(point);

		// when & then
		given()
			.when()
			.get("/api/v1/users/{id}/point", user.getId())
			.then()
			.log().all()
			.statusCode(HttpStatus.OK.value())
			.body("code", equalTo(200))
			.body("message", equalTo("OK"))
			.body("data.amount", equalTo(100_000));
	}

}
