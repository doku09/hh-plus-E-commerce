package kr.hhplus.be.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
@EnableJpaRepositories(basePackages = "kr.hhplus.be.server.infrastructure")
public class ServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServerApplication.class, args);
	}

}
