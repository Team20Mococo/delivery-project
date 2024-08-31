package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class UnauthorizedStoreAccessException extends BaseException {
	public UnauthorizedStoreAccessException() {
		super(ExceptionStatus.UNAUTHORIZED_STORE_ACCESS);
	}
}
