package com.spring.Final.modules.jobs.exceptions;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.http.HttpStatus;

public class ExceedFreeJobsAvailableException extends BaseException {
    public ExceedFreeJobsAvailableException() {
        super("EXCEED_FREE_JOBS_AVAILABLE", "Please use membership to post more jobs", HttpStatus.BAD_REQUEST);
    }
}
