package com.spring.Final.modules.jobs.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class JobInvalidStatusException extends BaseException {
    public JobInvalidStatusException(String message) {
        super("INVALID_STATUS", message, HttpStatus.BAD_REQUEST);
    }
}
