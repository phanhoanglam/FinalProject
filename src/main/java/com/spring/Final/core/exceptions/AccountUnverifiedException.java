package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class AccountUnverifiedException extends BaseException {
    public AccountUnverifiedException() {
        super("ACCOUNT_UNVERIFIED", "This account is not verified", HttpStatus.BAD_REQUEST);
    }
}

