package com.mococo.delivery.domain.exception;

public class BaseException extends RuntimeException {

	private final ExceptionStatus exceptionStatus;

	public BaseException(String message, ExceptionStatus exceptionStatus) {
		super(message);
		this.exceptionStatus = exceptionStatus;
	}

	public BaseException(ExceptionStatus exceptionStatus) {
		super(exceptionStatus.getMessage());
		this.exceptionStatus = exceptionStatus;
	}

	public ExceptionStatus getErrorCode() {
		return exceptionStatus;
	}
}
