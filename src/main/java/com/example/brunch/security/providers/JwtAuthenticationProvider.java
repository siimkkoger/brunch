package com.example.brunch.security.providers;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.brunch.security.JwtAuthenticationToken;
import com.example.brunch.security.roles.AbstractBrunchRole;
import com.example.brunch.util.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;

import java.util.Arrays;
import java.util.Collection;

public class JwtAuthenticationProvider implements AuthenticationProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationProvider.class);

    private final JwtService jwtService;

    public JwtAuthenticationProvider(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            final String token = (String) authentication.getCredentials();
            final DecodedJWT decodedJWT = jwtService.decodeJwtToken(token);
            final String username = decodedJWT.getSubject();
            final String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
            final Collection<GrantedAuthority> authorities =
                    Arrays.stream(roles).map(AbstractBrunchRole::createGrantedAuthority).toList();

            JwtAuthenticationToken authenticated = new JwtAuthenticationToken(authorities, token, username);
            LOGGER.debug("Successful jwt authentication!");
            return authenticated;
        } catch (Exception e) {
            LOGGER.error("Couldn't authenticate jwt: {}", e.getMessage());
            return authentication;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.equals(authentication);
    }
}