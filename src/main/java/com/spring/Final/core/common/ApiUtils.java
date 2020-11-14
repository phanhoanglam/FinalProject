package com.spring.Final.core.common;

import com.google.gson.Gson;
import com.spring.Final.core.exceptions.BaseException;
import com.spring.Final.core.infrastructure.ApiResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ApiUtils {

    private static final Gson gson = new Gson();

    public static final String HEADER_TOKEN = "Authorization";

    private static final int TOKEN_START_INDEX = 7;


    public static String getRequestToken(HttpServletRequest request) {
        final String bearerToken = request.getHeader(HEADER_TOKEN);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(TOKEN_START_INDEX);
        }
        return null;

    }

    public static void writeException(BaseException ex, HttpServletResponse response) {
        try {
            HashMap<String, String> result = new HashMap<String, String>();
            result.put("message", ex.getMessage());
            response.getWriter().write(gson.toJson(ApiResult.
                    builder()
                    .status(false)
                    .data(result)
                    .build()));
        } catch (Exception ignored) {

        }
    }

}

