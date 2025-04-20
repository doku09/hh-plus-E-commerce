package kr.hhplus.be.server.concurrent;

import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ConcurrencyLatchExecutor implements ConcurrencyExecutor{

	@Override
	public void execute(Runnable runnable, int threadCount) {

		ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
		// startLatch: 모든 스레드가 동시에 시작하게 만드는 장치. 스레드는 시작 전에 이 latch를 기다립니다.
		CountDownLatch startLatch = new CountDownLatch(1);
		// doneLatch: 모든 스레드가 작업을 마칠 때까지 대기하기 위한 장치.
		CountDownLatch doneLatch = new CountDownLatch(threadCount);

		// when
		for (int i = 0; i < threadCount; i++) {
			executorService.submit(() -> {
				try {
					// 모든 스레드 대기
					startLatch.await();
					runnable.run();
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} finally {
					doneLatch.countDown();
				}
			});
		}

		startLatch.countDown(); // 모든 스레드에 시작 신호 전파
		try {
			doneLatch.await(); // 모든 스레드 작업 완료될 때까지 대기
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		executorService.shutdown();
	}
}
