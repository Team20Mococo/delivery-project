package com.mococo.delivery.domain.exception.enumtype;

import com.mococo.delivery.domain.exception.ExceptionStatus;

public class RoleTypeException extends EnumTypeException{
	public RoleTypeException() {
		super(ExceptionStatus.ROLE_TYPE_NOT_FOUND);
	}
}
