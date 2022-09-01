package com.example.brunch.security.filters;

import com.example.brunch.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class BrunchBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BrunchBasicAuthenticationFilter.class);

    private final JwtUtils jwtUtils;

    public BrunchBasicAuthenticationFilter(
            final AuthenticationManager authenticationManager,
            final JwtUtils jwtUtils) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void onSuccessfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication auth) {
        final User account = (User) auth.getPrincipal();
        LOGGER.debug("Successful Basic Authentication, user: {}", account.getUsername());
        final String token = jwtUtils.createJwtToken(account.getUsername());
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        LOGGER.debug("Added a jwt token to header: {}", token);
    }

    @Override
    protected void onUnsuccessfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final AuthenticationException failed) {
        LOGGER.debug("Basic auth was unsuccessful: {}", failed.getMessage());
    }
}
