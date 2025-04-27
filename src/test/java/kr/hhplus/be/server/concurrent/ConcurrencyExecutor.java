package kr.hhplus.be.server.concurrent;

public interface ConcurrencyExecutor {

	void execute(Runnable runnable, int threadCount);
}
