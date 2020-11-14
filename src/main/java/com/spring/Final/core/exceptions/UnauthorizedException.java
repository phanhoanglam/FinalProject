package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class UnauthorizedException extends BaseException {
	public UnauthorizedException() {
		super("401", "Unauthorized", HttpStatus.UNAUTHORIZED);
	}
}
