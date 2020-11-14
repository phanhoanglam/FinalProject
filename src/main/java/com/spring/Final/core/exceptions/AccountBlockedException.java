package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class AccountBlockedException extends BaseException {
    public AccountBlockedException() {
        super("ACCOUNT_BLOCKED", "This account is blocked", HttpStatus.BAD_REQUEST);
    }
}
