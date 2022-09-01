package com.example.brunch;

import com.example.brunch.dbmodel.BrunchAccount;
import com.example.brunch.repository.BrunchAccountRepository;
import com.example.brunch.security.roles.AbstractBrunchRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
public class BrunchUserDetailsService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BrunchUserDetailsService.class);

    private final BrunchAccountRepository brunchAccountRepository;

    @Autowired
    public BrunchUserDetailsService(BrunchAccountRepository brunchAccountRepository) {
        this.brunchAccountRepository = brunchAccountRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final BrunchAccount account = brunchAccountRepository.findByUsername(username);
        if (account == null) {
            LOGGER.debug("User not found in the database: {}", username);
            throw new UsernameNotFoundException("User not found in the database: " + username);
        }
        LOGGER.debug("User found in the database: {}", username);
        return new User(
                account.getUsername(),
                account.getPassword(),
                account.isEnabled(),
                true,
                true,
                true,
                account.getRoles()
                        .stream()
                        .map(role -> AbstractBrunchRole.createGrantedAuthority(role.getCode()))
                        .collect(Collectors.toList()));
    }
}
