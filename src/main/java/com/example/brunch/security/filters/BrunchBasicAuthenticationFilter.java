package com.example.brunch.security.filters;

import com.example.brunch.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class BrunchBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BrunchBasicAuthenticationFilter.class);

    private final JwtUtils jwtUtils;

    public BrunchBasicAuthenticationFilter(
            final AuthenticationManager authenticationManager,
            final JwtUtils jwtUtils) {
        super(authenticationManager);
        this.jwtUtils = jwtUtils;
        LOGGER.debug("Created a custom BasicAuthenticationFilter.");
    }

    @Override
    protected void onSuccessfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Authentication auth) {
        LOGGER.debug("Successful Basic Authentication, user: {}", (String) auth.getPrincipal());
        final String token = jwtUtils.createJwtToken(((String) auth.getPrincipal()));
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        response.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + token);
        LOGGER.debug("Added a jwt token to header: {}", token);
    }
}
