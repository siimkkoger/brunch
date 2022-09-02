package com.example.brunch.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private UserDetails principal;
    private String token;

    public JwtAuthenticationToken(String token) {
        super(new ArrayList<>());
        this.token = token;
    }

    public JwtAuthenticationToken(UserDetails principal, String token) {
        super(principal.getAuthorities());
        this.principal = principal;
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setPrincipal(UserDetails principal) {
        this.principal = principal;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public UserDetails getPrincipal() {
        return principal;
    }

}