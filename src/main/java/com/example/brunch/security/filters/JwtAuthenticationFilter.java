package com.example.brunch.security.filters;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.brunch.security.TokenBasedAuthentication;
import com.example.brunch.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(final JwtUtils JWTUtils, final UserDetailsService userDetailsService) {
        this.jwtUtils = JWTUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain) throws IOException, ServletException {
        LOGGER.debug("Entered JwtAuthenticationFilter");
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Continue to next filter if Bearer token not provided
            filterChain.doFilter(request, response);
            return;
        }

        TokenBasedAuthentication authentication = null;
        final DecodedJWT decodedJWT = jwtUtils.decodeJwtToken(authHeader);
        final String username = decodedJWT.getSubject();
        if (username != null) {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            String token = authHeader.replace("Bearer ", "");
            authentication = new TokenBasedAuthentication(userDetails, token);
        }
        // TODO : try out auth0 JwtAuthenticationProvider instead
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}