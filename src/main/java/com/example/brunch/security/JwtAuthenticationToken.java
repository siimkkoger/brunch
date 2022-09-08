package com.example.brunch.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {

    private String username;
    private String token;

    public JwtAuthenticationToken(String token) {
        super(new ArrayList<>());
        this.token = token;
    }

    public JwtAuthenticationToken(Collection<GrantedAuthority> grantedAuthorities, String token, String username) {
        super(grantedAuthorities);
        this.token = token;
        this.username = username;
        this.setAuthenticated(true);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return username;
    }
}