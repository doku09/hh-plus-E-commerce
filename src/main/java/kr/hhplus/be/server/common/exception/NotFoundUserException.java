package kr.hhplus.be.server.common.exception;

public class NotFoundUserException extends RuntimeException{
	public NotFoundUserException() {
		super("사용자가 존재하지 않습니다.");
	}
}
