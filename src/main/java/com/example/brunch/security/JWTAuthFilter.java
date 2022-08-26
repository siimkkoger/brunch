package com.example.brunch.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.brunch.util.JWTUtils;
import com.example.brunch.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

/*
Got inspiration from
https://stackoverflow.com/questions/41975045/how-to-design-a-good-jwt-authentication-filter
 */
@Component
public class JWTAuthFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthFilter.class);

    private final SecurityUtils securityUtils;
    private final JWTUtils jwtUtils;

    @Autowired
    public JWTAuthFilter(final SecurityUtils securityUtils, final JWTUtils JWTUtils) {
        this.securityUtils = securityUtils;
        this.jwtUtils = JWTUtils;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain) throws IOException, ServletException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(jwtUtils.AUTHENTICATION_SCHEME_BEARER)) {
            filterChain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = null;
        try {
            final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (authHeader != null) {
                final DecodedJWT decodedJWT = jwtUtils.decodeJwtToken(authHeader);
                final String username = decodedJWT.getSubject();
                if (username != null) {
                    authentication = securityUtils.getAuthentication(username);
                }
            }

            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
            SecurityContextHolder.getContext().setAuthentication(null);
        } catch (final Exception e) {
            if (authentication == null) {
                LOGGER.info("Token creation failed.");
            }
            LOGGER.info("Unknown server error: " + e.getMessage());
        }
    }
}