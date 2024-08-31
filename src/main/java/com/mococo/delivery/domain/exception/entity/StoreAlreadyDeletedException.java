package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class StoreAlreadyDeletedException extends BaseException {
	public StoreAlreadyDeletedException() {
		super(ExceptionStatus.STORE_ALREADY_DELETED);
	}
}