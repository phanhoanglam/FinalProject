package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidAddressException extends BaseException {
    public InvalidAddressException() {
        super("INVALID_ADDRESS", "This address is invalid", HttpStatus.BAD_REQUEST);
    }
}
