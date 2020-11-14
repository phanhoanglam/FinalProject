package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class TokenInvalidException extends BaseException {
	public TokenInvalidException() {
		super("401", "Invalid token", HttpStatus.UNAUTHORIZED);
	}
}
