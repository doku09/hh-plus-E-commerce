package kr.hhplus.be.server.interfaces.api.user.response;

import kr.hhplus.be.server.domain.user.UserInfo;

public record UserResponse(long id, String name){

	public static UserResponse from(UserInfo userInfo) {
		return new UserResponse(userInfo.id(), userInfo.name());
	}
}

