package com.spring.Final.modules.payment.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class PaymentFailedException extends BaseException {
    public PaymentFailedException(String message) {
        super("PAYMENT_FAILED", message, HttpStatus.BAD_REQUEST);
    }
}
