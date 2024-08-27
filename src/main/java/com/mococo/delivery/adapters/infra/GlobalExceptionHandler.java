package com.mococo.delivery.adapters.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mococo.delivery.domain.exception.business.BusinessException;
import com.mococo.delivery.domain.exception.enumtype.EnumTypeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * enum Type 불일치 Exception
	 */
	@ExceptionHandler(EnumTypeException.class)
	protected ResponseEntity<ExceptionResponse> handleEnumTypeBinding(EnumTypeException error) {
		log.error("HANDLE ERROR - {}", error.toString());
		ExceptionResponse exceptionResponse = ExceptionResponse.from(error);
		return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
	}

	/**
	 * Business Exception
	 */
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ExceptionResponse> handleEntityNotFound(BusinessException error) {
		log.error("HANDLE ERROR - {}", error.toString());
		ExceptionResponse exceptionResponse = ExceptionResponse.from(error);
		return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
	}
}
