package com.spring.Final.core.infrastructure;

import java.util.HashMap;

public final class ApiErrors {

    public static final HashMap<String, String> USER_PASSWORD_NOT_FOUND = new HashMap<>();
    public static final HashMap<String, String> USERNAME_CONFLICT = new HashMap<>();

    static {
        USER_PASSWORD_NOT_FOUND.put("error_code", "USER_PASSWORD_NOT_FOUND");
        USER_PASSWORD_NOT_FOUND.put("message", "Username or password is not found!");

        USERNAME_CONFLICT.put("error_code", "USERNAME_CONFLICT");
        USERNAME_CONFLICT.put("message", "Username already exists!");
    }

}
