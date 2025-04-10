package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCommand;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OrderCreateCommand {

	private  int totalPrice;
	private  OrderStatus status;
	private  User user;
	private  List<OrderItem> orderItems;

	private OrderCreateCommand(OrderStatus status, User user, List<OrderItem> orderItems) {
		this.status = status;
		this.user = user;
		this.orderItems = orderItems;
	}

	public static OrderCreateCommand of(OrderStatus status, User user) {
		return new OrderCreateCommand(status, user, new ArrayList<>());
	}

	public void changeStatus (OrderStatus status) {
		this.status = status;
	}

	public void addItem(OrderItem orderItem) {
		this.orderItems.add(orderItem);
	}
}
