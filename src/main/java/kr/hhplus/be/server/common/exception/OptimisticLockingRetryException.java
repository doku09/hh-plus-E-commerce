package kr.hhplus.be.server.common.exception;

public class OptimisticLockingRetryException extends RuntimeException {
	public OptimisticLockingRetryException() {
		super("문제가 발생하였습니다. 잠시후 다시 시도해주세요.");
	}
}
