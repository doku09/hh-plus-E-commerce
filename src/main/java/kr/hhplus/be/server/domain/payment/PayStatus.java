package kr.hhplus.be.server.domain.payment;

public enum PayStatus {
	READY("결제 준비"),
	COMPLETE("결제 완료"),
	CANCEL("결제 취소");

	private String description;

	PayStatus(String description) {
		this.description = description;
	}
}
