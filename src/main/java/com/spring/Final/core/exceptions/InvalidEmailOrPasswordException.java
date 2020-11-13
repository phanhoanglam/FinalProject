package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidEmailOrPasswordException extends BaseException {
    public InvalidEmailOrPasswordException() {
        super("INVALID_EMAIL_OR_PASSWORD", "Email or password is invalid", HttpStatus.BAD_REQUEST);
    }
}
