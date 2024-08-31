package com.mococo.delivery.adapters.infra;

import org.springframework.http.HttpStatus;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExceptionResponse {
	private HttpStatus status;
	private String exceptionCode;
	private String message;

	public ExceptionResponse(final ExceptionStatus exceptionStatus) {
		this.status = exceptionStatus.getHttpStatus();
		this.exceptionCode = exceptionStatus.getExceptionCode();
		this.message = exceptionStatus.getMessage();
	}

	public static ExceptionResponse from(ExceptionStatus exceptionStatus) {
		return new ExceptionResponse(exceptionStatus);
	}

	public static ExceptionResponse from(BaseException error) {
		return new ExceptionResponse(error.getErrorCode());
	}
}
