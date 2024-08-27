package com.mococo.delivery.domain.exception;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ExceptionStatus {

	//	enum type binding exception
	ROLE_TYPE_NOT_FOUND(HttpStatus.BAD_REQUEST, "Enum-001", "요청에 대한 UserRole Enum Type 을 찾을 수 없음"),

	// business policy
	FIVE_MINUTES_OVER(HttpStatus.BAD_REQUEST, "Business-001", "5분이 지나 주문을 취소할 수 없습니다");

	private final HttpStatus httpStatus;
	private final String exceptionCode;
	private final String message;
}
