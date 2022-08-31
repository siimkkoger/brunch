package com.example.brunch.security.roles;

import org.springframework.security.core.GrantedAuthority;

/**
 * Spring @Secured annotation by default looks for "ROLE_" in the type to separate roles from permissions
 * It can be changed if wished.
 */
public interface BrunchRole extends GrantedAuthority {

    String USER = "ROLE_USER";
    String ADMIN = "ROLE_ADMIN";

    String getRole();

    @Override
    default String getAuthority() {
        return getRole();
    }
}
