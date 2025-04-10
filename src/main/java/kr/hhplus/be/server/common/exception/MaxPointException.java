package kr.hhplus.be.server.common.exception;

public class MaxPointException extends RuntimeException{
	public MaxPointException() {
		super("최대 포인트를 초과하였습니다.");
	}
}
