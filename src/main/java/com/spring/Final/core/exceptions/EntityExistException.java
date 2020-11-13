package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class EntityExistException extends BaseException {
    public EntityExistException(String message) {
        super("ACCOUNT_EXISTS", message, HttpStatus.BAD_REQUEST);
    }
}
