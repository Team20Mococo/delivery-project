package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class ProductAlreadyDeletedException extends BaseException {
    public ProductAlreadyDeletedException() {
        super(ExceptionStatus.PRODUCT_ALREADY_DELETED);
    }
}
