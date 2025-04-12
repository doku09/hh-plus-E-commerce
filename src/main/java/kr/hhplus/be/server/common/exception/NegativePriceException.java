package kr.hhplus.be.server.common.exception;

public class NegativePriceException extends RuntimeException {
	public NegativePriceException() {
		super("가격은 음수가 될 수 없습니다.");
	}
}
