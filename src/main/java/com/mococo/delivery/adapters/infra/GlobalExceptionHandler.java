package com.mococo.delivery.adapters.infra;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mococo.delivery.domain.exception.business.BusinessException;
import com.mococo.delivery.domain.exception.entity.EntityException;
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
		log.error("Enum ERROR - {}", error.toString());
		ExceptionResponse exceptionResponse = ExceptionResponse.from(error);
		return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
	}

	/**
	 * Business Exception
	 */
	@ExceptionHandler(BusinessException.class)
	protected ResponseEntity<ExceptionResponse> handleBusiness(BusinessException error) {
		log.error("Business ERROR - {}", error.toString());
		ExceptionResponse exceptionResponse = ExceptionResponse.from(error);
		return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
	}
	/**
	 * Business Exception
	 */
	@ExceptionHandler(EntityException.class)
	protected ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityException error) {
		log.error("Entity ERROR - {}", error.toString());
		ExceptionResponse exceptionResponse = ExceptionResponse.from(error);
		return new ResponseEntity<>(exceptionResponse, exceptionResponse.getStatus());
	}
}
