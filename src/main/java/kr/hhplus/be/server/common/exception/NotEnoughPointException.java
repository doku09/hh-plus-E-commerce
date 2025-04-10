package kr.hhplus.be.server.common.exception;

public class NotEnoughPointException extends RuntimeException{
	public NotEnoughPointException() {
		super("잔여 포인트가 부족합니다.");
	}
}
