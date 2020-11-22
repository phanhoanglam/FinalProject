package com.spring.Final.core.config;

import com.spring.Final.core.exceptions.BaseException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String handle(HttpServletRequest request, Exception e) {
        System.out.println(e);

        return "client/modules/pages/error/error-page";
    }

    @ExceptionHandler(BaseException.class)
    public String handleExceptions(BaseException e, WebRequest webRequest) {
        System.out.println(e);

        return "client/modules/pages/error/" + e.getHttpStatus().value();
    }
}

