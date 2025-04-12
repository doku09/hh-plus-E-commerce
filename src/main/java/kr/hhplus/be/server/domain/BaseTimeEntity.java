package kr.hhplus.be.server.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

/**
 * 추상 클래스
 */
@Getter
public abstract class BaseTimeEntity {

	@CreatedDate
	protected LocalDateTime createdAt;

	@LastModifiedDate
	protected LocalDateTime updatedAt;
}
