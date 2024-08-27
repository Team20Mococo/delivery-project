package com.mococo.delivery.domain.exception.business;

import com.mococo.delivery.domain.exception.ExceptionStatus;

public class LateOrderCancelException extends BusinessException {
	public LateOrderCancelException() {
		super(ExceptionStatus.FIVE_MINUTES_OVER);
	}
}
