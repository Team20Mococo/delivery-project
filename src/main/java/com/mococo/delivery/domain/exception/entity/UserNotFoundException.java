package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.ExceptionStatus;

public class UserNotFoundException extends EntityException{
	public UserNotFoundException() {
		super(ExceptionStatus.USER_NOT_FOUND);
	}
}
