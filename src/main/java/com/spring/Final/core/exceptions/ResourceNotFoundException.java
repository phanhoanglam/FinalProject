package com.spring.Final.core.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException() {
        super("RESOURCE_NOT_FOUND", "Resource not found", HttpStatus.NOT_FOUND);
    }
}
