package kr.hhplus.be.server.common.exception;

import org.springframework.http.HttpStatus;

public record ErrorResult(String message, HttpStatus httpStatus) {
}
