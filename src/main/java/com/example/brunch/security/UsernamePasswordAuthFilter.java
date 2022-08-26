package com.example.brunch.security;

import com.example.brunch.form.LoginForm;
import com.example.brunch.util.JWTUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class UsernamePasswordAuthFilter extends UsernamePasswordAuthenticationFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(UsernamePasswordAuthFilter.class);

    private final SecurityUtils securityUtils;
    private final JWTUtils jwtUtils;

    @Autowired
    public UsernamePasswordAuthFilter(
            AuthenticationManager authenticationManager,
            SecurityUtils securityUtils,
            JWTUtils jwtUtils) {
        super(authenticationManager);
        this.securityUtils = securityUtils;
        this.jwtUtils = jwtUtils;
    }

    @Override
    public Authentication attemptAuthentication(
            final HttpServletRequest req,
            final HttpServletResponse res) throws AuthenticationException {
        try {
            final LoginForm loginRequest = new ObjectMapper().readValue(req.getInputStream(),
                    LoginForm.class);

            UsernamePasswordAuthenticationToken token =
                    securityUtils.getAuthentication(loginRequest.username(),
                            loginRequest.password());
            return this.getAuthenticationManager().authenticate(token);
        } catch (final Exception e) {
            LOGGER.info(String.format("Exception during authentication (%s) ", e.getMessage()));
        }
        return null;
    }

    @Override
    protected void successfulAuthentication(
            final HttpServletRequest req,
            final HttpServletResponse res,
            final FilterChain chain,
            final Authentication auth) {
        final String token = jwtUtils.createJwtToken(((String) auth.getPrincipal()));
        res.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        res.addHeader(HttpHeaders.AUTHORIZATION, jwtUtils.addBearerPrefixToJwtToken(token));
    }
}
