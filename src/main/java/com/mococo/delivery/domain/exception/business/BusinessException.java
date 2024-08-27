package com.mococo.delivery.domain.exception.business;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class BusinessException extends BaseException {
	public BusinessException(ExceptionStatus exceptionStatus) {
		super(exceptionStatus);
	}
}
