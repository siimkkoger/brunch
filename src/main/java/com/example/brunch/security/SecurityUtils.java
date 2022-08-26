package com.example.brunch.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityUtils.class);

    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityUtils(final UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String username) {
        return getAuthentication(username, null);
    }

    public UsernamePasswordAuthenticationToken getAuthentication(final String username, final String password) {
        try {
            final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return new UsernamePasswordAuthenticationToken(
                    userDetails.getUsername(),
                    password,
                    userDetails.getAuthorities());
        } catch (final Exception e) {
            // TODO : error handling
            LOGGER.info(String.format("Error during authentication: %s", e.getMessage()));
            return null;
        }
    }
}
