package com.spring.Final.modules.review.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class ExistingReviewException extends BaseException {
    public ExistingReviewException() {
        super("REVIEW_EXISTS", "This review already exists", HttpStatus.BAD_REQUEST);
    }
}
