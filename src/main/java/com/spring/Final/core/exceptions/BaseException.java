package com.spring.Final.core.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
public abstract class BaseException extends RuntimeException {
	private String code;
	private String message;
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
}
