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
	public ResponseEntity<ErrorResult> argumentNotValidException(NotEnoughPointException e) {

		return new ResponseEntity<>(new ErrorResult(e.getMessage(),HttpStatus.BAD_REQUEST),HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler
	public ResponseEntity<ErrorResult> globalException(GlobalBusinessException e) {
		return new ResponseEntity<>(new ErrorResult(e.getMessage(),e.getHttpStatus()),e.getHttpStatus());
	}
}
