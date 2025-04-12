package kr.hhplus.be.server.domain.product;


import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.common.exception.NegativePriceException;
import kr.hhplus.be.server.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
public class Product extends BaseTimeEntity {

	@Setter // 메모리 DB개발용
	private long id;
	private final String name;
	private final int price;

	private Product(String name, int price) {
		if (price < 0) throw new NegativePriceException();
		if (name.trim().isEmpty()) throw new GlobalBusinessException(ErrorCode.NOT_EMPTY_PRICE_NAME);

		this.name = name;
		this.price = price;
	}

	public Product(long id, String name, int price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	// TODO QUESTION) 아이디가 있는생성자(수정시) 아이디가 없는생성(생성시) 네이밍이궁금하고 이런식으로 필드가 많아지면 생성자가 많아지는 현상은 괜찮을까요?
	public static Product create(String name, int price) {
		return new Product(name, price);
	}

	public static Product of(long id, String name, int price) {
		return new Product(id, name, price);
	}
}
