package com.example.brunch.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.brunch.util.JWTUtils;
import com.example.brunch.util.jwtUtils;
import com.example.brunch.util.jwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BrunchBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(BrunchBasicAuthenticationFilter.class);

    private final SecurityUtils securityUtils;
    private final JWTUtils jwtUtils;

    @Autowired
    public BrunchBasicAuthenticationFilter(
            final AuthenticationManager authenticationManager,
            final SecurityUtils securityUtils,
            final JWTUtils jwtUtils) {
        this.securityUtils = securityUtils;
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain) throws IOException, ServletException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (header == null || !header.startsWith(jwtUtils.JWT_BEARER_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken token = null;
        try {
            final String reqAuthHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (reqAuthHeader != null) {
                final DecodedJWT decodedJWT = jwtUtils.decodeJwtToken(reqAuthHeader);
                final String username = decodedJWT.getSubject();
                if (username != null) {
                    token = securityUtils.createUsernamePasswordAuthToken(username);
                }
            }

            SecurityContextHolder.getContext().setAuthentication(token);
            onSuccessfulAuthentication(request, response, token);
            chain.doFilter(request, response);
        } catch (final Exception e) {
            if (token == null) {
                throw new BcApiException(BcApiException.Type.AUTHORIZATION, "Token creation " +
                        "failed");
            }
            throw new BcApiException(BcApiException.Type.SERVER_ERROR, "Unknown server error");
        }
    }

    protected void successfulAuthentication(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain chain,
            final Authentication auth) {
        final String token = jwtUtils.createJwtToken(((String) auth.getPrincipal()));
        response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS, HttpHeaders.AUTHORIZATION);
        response.addHeader(HttpHeaders.AUTHORIZATION, jwtUtils.addBearerPrefixToJwtToken(token));
    }
}
