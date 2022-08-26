package com.example.brunch.security;

import com.example.brunch.EventController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class BrunchAuthenticationProvider implements AuthenticationProvider {

    Logger logger = LoggerFactory.getLogger(BrunchAuthenticationProvider.class);

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BcUserService bcUserService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {
        final UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        final String tokenPassword = (String) token.getCredentials();
        final String tokenUsername = (String) token.getPrincipal();
        final BcUser user = bcUserService.findByEmail(tokenUsername);

        // check if raw password encrypted matches the one stored in db
        final boolean passwordsMatch = passwordEncoder.matches(tokenPassword, user.getPassword());

        if (!passwordsMatch) {
            // TODO : exception handling
            logger.info(String.format("Invalid username (%s) / password (%s)", tokenUsername, tokenPassword));
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(tokenUsername);
        return new UsernamePasswordAuthenticationToken(
                user.getEmail(),
                user.getPassword(),
                userDetails.getAuthorities());
    }

    @Override
    public boolean supports(final Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.equals(authentication);
    }
}
