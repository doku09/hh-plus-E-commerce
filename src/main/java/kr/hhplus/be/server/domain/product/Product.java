package kr.hhplus.be.server.domain.product;


public class Product {

	private long id;
	private String name;
	private int price;

	private Product(String name, int price) {
		this.name = name;
		this.price = price;
	}

	//TODO QUESTION) 아이디가 있는생성자(수정시) 아이디가 없는생성(생성시) 네이밍이궁금하고 이런식으로 필드가 많아지면 생성자가 많아지는 현상은 괜찮을까요?
	public static Product create(String name, int price) {
		return new Product(name,price);
	}
}
