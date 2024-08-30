package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.ExceptionStatus;

public class PasswordNotMatchedException extends EntityException {
	public PasswordNotMatchedException() {
		super(ExceptionStatus.PASSWORD_NOT_MATCHED);
	}
}
