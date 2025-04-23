package kr.hhplus.be.server.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {
	NOT_EMPTY_PRICE_NAME("상품명을 입력하지 않았습니다.",HttpStatus.BAD_REQUEST),
	NOT_FOUND_PRODUCT("상품이 존재하지 않습니다.",HttpStatus.NOT_FOUND),
	NOT_FOUND_COUPON("쿠폰이 존재하지 않습니다.",HttpStatus.NOT_FOUND),
	NOT_FOUND_STOCK("상품 재고가 존재하지 않습니다.",HttpStatus.NOT_FOUND),
	NOT_ENOUGH_STOCK("상품 재고가 충분하지 않습니다.",HttpStatus.BAD_REQUEST),
	NOT_ENOUGH_COUPON("쿠폰 수량이 초과되었습니다.", HttpStatus.BAD_REQUEST),
	ALREADY_ISSUED_COUPON("이미 발급된 쿠폰입니다.", HttpStatus.BAD_REQUEST),
	ALREADY_USED_COUPON("이미 사용된 쿠폰입니다.", HttpStatus.BAD_REQUEST),
	NOT_HAVE_COUPON("해당 유저가 보유한 쿠폰이 아닙니다.", HttpStatus.BAD_REQUEST),
	INVALID_COUPON_QUANTITY("수량제한이 있는 쿠폰은 1개 이상 등록할 수 있습니다.", HttpStatus.BAD_REQUEST);


	ErrorCode(String message, HttpStatus httpStatus) {
		this.message = message;
		this.httpStatus = httpStatus;
	}

	private String message;
	private HttpStatus httpStatus;
}
