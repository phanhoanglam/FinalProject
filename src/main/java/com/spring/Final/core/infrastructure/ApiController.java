package com.spring.Final.core.infrastructure;

import com.spring.Final.core.common.ApiUtils;
import com.spring.Final.core.common.JwtHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public abstract class ApiController {
    protected HashMap<String, Object> getCurrentUser(HttpServletRequest request) {
        String token = ApiUtils.getRequestToken(request);

        return JwtHelper.validateToken(token);
    }

    protected HttpServletRequest getCurrentRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    protected ResponseEntity<ApiResult> buildResponse(Object data) {
        return new ResponseEntity<>(
                ApiResult.builder()
                        .status(true)
                        .code("Ok")
                        .data(data)
                        .build(),
                HttpStatus.OK);
    }

    protected ResponseEntity<ApiResult> buildResponse(
        HttpStatus httpStatus,
        Object data,
        String code
    ) {
        return new ResponseEntity<>(
                ApiResult.builder()
                        .status(false)
                        .code(code)
                        .data(data)
                        .build(),
                httpStatus);
    }
}
