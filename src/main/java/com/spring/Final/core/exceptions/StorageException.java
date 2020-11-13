package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class StorageException extends BaseException {
    public StorageException(String code, String message) {
        super(code, message, HttpStatus.BAD_REQUEST);
    }
}
