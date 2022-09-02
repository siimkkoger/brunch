package com.example.brunch.security.providers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.brunch.security.JwtAuthenticationToken;
import com.example.brunch.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationProvider(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            JwtAuthenticationToken jwtAuth = (JwtAuthenticationToken) authentication;
            String token = (String) jwtAuth.getCredentials();
            final DecodedJWT decodedJWT = jwtService.decodeJwtToken(token);
            final String username = decodedJWT.getSubject();
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(userDetails, token);
            authenticated.setAuthenticated(true);
            LOGGER.debug("Successful jwt authentication!");
            return authenticated;
        } catch (Exception e) {
            LOGGER.error("Couldn't authenticate jwt: {}", e.getMessage());
            return authentication;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        LOGGER.debug("Does jwt auth support authentication?");
        return JwtAuthenticationToken.class.equals(authentication);
    }
}