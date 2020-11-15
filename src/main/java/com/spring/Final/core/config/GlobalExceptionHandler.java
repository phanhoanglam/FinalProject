package com.spring.Final.core.config;

import com.spring.Final.core.exceptions.BaseException;
import com.spring.Final.core.infrastructure.ApiResult;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResult> handle(HttpServletRequest request, Exception e) {
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("message", e.getMessage());
        System.out.println(e);

        return new ResponseEntity<>(
                ApiResult
                        .builder()
                        .status(false)
                        .data(data)
                        .code("INTERNAL_SERVER_ERROR")
                        .build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<Object> handleExceptions(BaseException e, WebRequest webRequest) {
        HashMap<String, String> data = new HashMap<>();
        data.put("message", e.getMessage());
        System.out.println(e);

        return new ResponseEntity<>(
                ApiResult
                        .builder()
                        .status(false)
                        .code(e.getCode())
                        .data(data)
                        .build(),
                e.getHttpStatus());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        BindingResult bindingResult = e.getBindingResult();
        List<ObjectError> errorMessages = bindingResult.getAllErrors();
        System.out.println(e);

        return new ResponseEntity<>(
                ApiResult
                        .builder()
                        .status(false)
                        .data(errorMessages)
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException e,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        HashMap<String, String> data = new HashMap<>();
        data.put("message", e.getMessage());
        System.out.println(e);

        return new ResponseEntity<>(
                ApiResult
                        .builder()
                        .status(false)
                        .data(data)
                        .code("BAD_REQUEST")
                        .build(),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(EntityNotFoundException e, WebRequest request) {
        HashMap<String, String> data = new HashMap<>();
        data.put("message", "Resource not found");
        System.out.println(e);

        return new ResponseEntity<>(
                ApiResult
                        .builder()
                        .status(false)
                        .data(data)
                        .code("RESOURCE_NOT_FOUND")
                        .build(),
                HttpStatus.NOT_FOUND);
    }
}

