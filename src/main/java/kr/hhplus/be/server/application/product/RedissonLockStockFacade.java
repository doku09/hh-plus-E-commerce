package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
@Slf4j
public class RedissonLockStockFacade{

	private final RedissonClient redissonClient;
	private final ProductService stockService;

	public void deductStock(ProductCommand.DeductStock command) {

		String lockKey = "lock:product:" + command.getProductId();
		String channelName = "lock:channel:product" + command.getProductId();
		log.info("락 키: {}", lockKey);
		long timeOutMs = 1000; // 10초
		long startTime = System.currentTimeMillis();

		while(true) {

			RBucket<String> lockBucket = redissonClient
				.getBucket(lockKey);

			boolean acquired = lockBucket.setIfAbsent("LOCKED", Duration.ofSeconds(5));
			log.info("[{}] 락 잡힘 여부: {}",Thread.currentThread().getName(),acquired);
			if(Boolean.TRUE.equals(acquired)) {
				log.info("[{}] Lock 획득 성공",Thread.currentThread().getName());

				try {
					stockService.deductStock(command);
				} finally {
					lockBucket.delete();
					redissonClient.getTopic(channelName).publish("UNLOCK");
					log.info("[{}] Lock 해제 알림",Thread.currentThread().getName());
				}
				return;
			}

			CountDownLatch latch = new CountDownLatch(1);
			RTopic topic = redissonClient.getTopic(channelName);
			int listenerId = topic.addListener(String.class, (channel, message) -> {
				if ("UNLOCK".equals(message)) {
					latch.countDown();
				}
			});

			try {
				long elapsed = System.currentTimeMillis() - startTime;
				long remainTimeout = timeOutMs - elapsed;

				if(remainTimeout <= 0) {
					log.error("[{}] Lock 획득 실패 (Timeout)",Thread.currentThread().getName());
					throw new RuntimeException("락 획득 실패 (Timeout");
				}

				latch.await(remainTimeout, TimeUnit.SECONDS);

			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				throw new RuntimeException(e);
			}finally {
				topic.removeListener(listenerId);
			}
		}
	}
}
