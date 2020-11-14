package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class BadRequestException extends BaseException {
    public BadRequestException(String message) {
        super("BAD_REQUEST", message, HttpStatus.BAD_REQUEST);
    }
}
