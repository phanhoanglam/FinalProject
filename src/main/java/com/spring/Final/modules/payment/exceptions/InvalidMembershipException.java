package com.spring.Final.modules.payment.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class InvalidMembershipException extends BaseException {
    public InvalidMembershipException() {
        super("INVALID_MEMBERSHIP", "Invalid membership", HttpStatus.BAD_REQUEST);
    }
}
