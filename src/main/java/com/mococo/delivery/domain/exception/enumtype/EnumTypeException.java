package com.mococo.delivery.domain.exception.enumtype;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class EnumTypeException extends BaseException {
	public EnumTypeException(ExceptionStatus exceptionStatus) {
		super(exceptionStatus);
	}
}
