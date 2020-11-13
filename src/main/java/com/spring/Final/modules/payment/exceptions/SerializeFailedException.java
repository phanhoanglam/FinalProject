package com.spring.Final.modules.payment.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class SerializeFailedException extends BaseException {
    public SerializeFailedException() {
        super("SERIALIZE_FAILED", "Please contact admin to find more information", HttpStatus.BAD_REQUEST);
    }
}