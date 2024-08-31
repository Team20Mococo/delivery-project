package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class StoreNotFoundException extends BaseException {
	public StoreNotFoundException() {
		super(ExceptionStatus.STORE_NOT_FOUND);
	}
}
