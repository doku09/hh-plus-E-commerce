package kr.hhplus.be.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GlobalBusinessException extends RuntimeException {

	private String message;
	private HttpStatus httpStatus;

	public GlobalBusinessException(String message) {
		super(message);
	}

	public GlobalBusinessException(ErrorCode error) {
		super(error.getMessage());
		this.message = error.getMessage();
		this.httpStatus = error.getHttpStatus();
	}
}
