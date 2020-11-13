package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class InternalServerException extends BaseException {
	public InternalServerException(String message) {
		super("INTERNAL_SERVER_ERROR", message, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
