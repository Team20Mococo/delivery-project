package com.mococo.delivery.application.dto;

import lombok.Getter;

@Getter
public class SuccessResponseDto<T> {
    public static final String CODE = "001";

    private final String code;
    private final String message;
    private final T data;

    public SuccessResponseDto(String message, T data) {
        this.code = CODE;
        this.message = message;
        this.data = data;
    }
}
