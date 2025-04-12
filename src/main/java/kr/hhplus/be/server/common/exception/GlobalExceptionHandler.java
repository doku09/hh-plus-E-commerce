package kr.hhplus.be.server.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public GlobalBusinessException argumentNotValidException(MethodArgumentNotValidException e) {

		return null;
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> globalException(GlobalBusinessException e) {
		return new ResponseEntity<>(new ErrorResult(e.getMessage(),e.getHttpStatus()),e.getHttpStatus());
	}
}
