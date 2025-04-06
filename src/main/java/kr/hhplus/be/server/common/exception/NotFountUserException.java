package kr.hhplus.be.server.common.exception;

public class NotFountUserException extends RuntimeException{
	public NotFountUserException() {
		super("사용자가 존재하지 않습니다.");
	}
}
