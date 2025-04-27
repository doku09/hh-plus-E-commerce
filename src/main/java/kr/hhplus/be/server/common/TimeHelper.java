package kr.hhplus.be.server.common;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class TimeHelper {

	public void printTime() {
		long millis = System.currentTimeMillis();
		LocalDateTime dateTime = Instant.ofEpochMilli(millis)
			.atZone(ZoneId.systemDefault())
			.toLocalDateTime();

		String formatted = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

		System.out.println("[실행시간] " + formatted);
	}

}
