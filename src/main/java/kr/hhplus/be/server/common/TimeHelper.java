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


	public void test() {
//		CompletableFuture<RedissonLockEntry> subscribeFuture = subscribe(threadId);
//		try {
//			subscribeFuture.get(time, TimeUnit.MILLISECONDS);
//		} ...
//
//		while (true) {
//			long currentTime = System.currentTimeMillis();
//			ttl = tryAcquire(waitTime, leaseTime, unit, threadId);
//			// lock acquired
//			if (ttl == null) {
//				return true;
//			}
//
//                ...
//
//			// waiting for message
//			currentTime = System.currentTimeMillis();
//			if (ttl >= 0 && ttl < time) {
//				commandExecutor.getNow(subscribeFuture).getLatch().tryAcquire(ttl, TimeUnit.MILLISECONDS);
//			} else {
//				commandExecutor.getNow(subscribeFuture).getLatch().tryAcquire(time, TimeUnit.MILLISECONDS);
//			}
//
//			time -= System.currentTimeMillis() - currentTime;
//                ....
//		} finally{
//			unsubscribe(commandExecutor.getNow(subscribeFuture), threadId);
//		}
	}


}
