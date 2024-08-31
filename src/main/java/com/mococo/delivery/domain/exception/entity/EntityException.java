package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class EntityException extends BaseException {
	public EntityException(ExceptionStatus exceptionStatus) {
		super(exceptionStatus);
	}
}
