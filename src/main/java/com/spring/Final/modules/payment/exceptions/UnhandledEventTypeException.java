package com.spring.Final.modules.payment.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class UnhandledEventTypeException extends BaseException {
    public UnhandledEventTypeException() {
        super("UNHANDLE_EVENT_TYPE", "Please contact admin to find more information", HttpStatus.BAD_REQUEST);
    }
}