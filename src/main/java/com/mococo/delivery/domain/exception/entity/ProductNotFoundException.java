package com.mococo.delivery.domain.exception.entity;

import com.mococo.delivery.domain.exception.BaseException;
import com.mococo.delivery.domain.exception.ExceptionStatus;

public class ProductNotFoundException extends BaseException {
    public ProductNotFoundException() {
        super(ExceptionStatus.PRODUCT_NOT_FOUND);
    }
}
