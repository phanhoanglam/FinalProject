package com.spring.Final.modules.payment.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class PaymentFailedException extends BaseException {
    public PaymentFailedException() {
        super("PAYMENT_FAILED", "Please contact admin to find more information", HttpStatus.BAD_REQUEST);
    }
}
