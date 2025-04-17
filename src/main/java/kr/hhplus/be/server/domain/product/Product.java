package kr.hhplus.be.server.domain.product;


import jakarta.persistence.*;
import kr.hhplus.be.server.common.exception.ErrorCode;
import kr.hhplus.be.server.common.exception.GlobalBusinessException;
import kr.hhplus.be.server.common.exception.NegativePriceException;
import kr.hhplus.be.server.domain.BaseTimeEntity;
import lombok.Getter;
import lombok.Setter;


@Getter
@Entity
public class Product extends BaseTimeEntity {

	@Column(name = "product_id")
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String name;
	private Long price;

	private Product(String name, Long price) {
		if (price < 0) throw new NegativePriceException();
		if (name.trim().isEmpty()) throw new GlobalBusinessException(ErrorCode.NOT_EMPTY_PRICE_NAME);

		this.name = name;
		this.price = price;
	}

	public Product(Long id, String name, Long price) {
		this.id = id;
		this.name = name;
		this.price = price;
	}

	public Product() {

	}

	public static Product create(String name, Long price) {
		return new Product(name, price);
	}

	public static Product of(Long id, String name, Long price) {
		return new Product(id, name, price);
	}
}
