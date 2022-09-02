package com.example.brunch.security.roles;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;

public abstract class  AbstractBrunchRole implements BrunchRole {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractBrunchRole.class);

    private final String role;

    public AbstractBrunchRole(final String role) {
        this.role = role;
    }

    public static GrantedAuthority createGrantedAuthority(final String code) {
        switch (code) {
            case ROLE_ADMIN:
                return new AdminRole();
            case ROLE_USER:
                return new UserRole();
            default:
                LOGGER.error("No such role found: {}", code);
                return null;
        }
    }

    @Override
    public String getRole() {
        return role;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof BrunchRole) {
            return getRole().equals(((BrunchRole) obj).getRole());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return role.hashCode();
    }

    @Override
    public String toString() {
        return "Role: " + role;
    }
}
