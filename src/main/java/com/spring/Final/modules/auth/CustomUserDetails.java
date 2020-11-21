package com.spring.Final.modules.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
import java.util.HashMap;

public class CustomUserDetails  extends User {
    private HashMap<String, Object> information;

    public CustomUserDetails(String username, String password, HashMap<String, Object> information, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
        this.information = information;
    }

    public HashMap<String, Object> getInformation() {
        return information;
    }
}
