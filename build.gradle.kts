plugins {
	java
	id("org.springframework.boot") version "3.4.1"
	id("io.spring.dependency-management") version "1.1.7"
}

fun getGitHash(): String {
	return providers.exec {
		commandLine("git", "rev-parse", "--short", "HEAD")
	}.standardOutput.asText.get().trim()
}

group = "kr.hhplus.be"
version = getGitHash()

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
	}
}
val restAssuredVersion = "5.3.2"
dependencies {
    // Spring
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")

	//Lombok
	implementation("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")

    // DB
	runtimeOnly("com.mysql:mysql-connector-j")

	//Swagger
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")

	// retry 적용
	implementation ("org.springframework.retry:spring-retry")
	implementation ("org.springframework:spring-aspects")

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:mysql")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

	//redis
	implementation ("org.redisson:redisson-spring-boot-starter:3.23.4")
	implementation ("com.fasterxml.jackson.datatype:jackson-datatype-jsr310")
	//restAssured
	testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")
	testImplementation("io.rest-assured:json-path:${restAssuredVersion}")
	testImplementation("io.rest-assured:json-schema-validator:${restAssuredVersion}")
}

tasks.withType<Test> {
	useJUnitPlatform()
	systemProperty("user.timezone", "UTC")
}
