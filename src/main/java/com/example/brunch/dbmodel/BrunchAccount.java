package com.example.brunch.dbmodel;

import org.hibernate.validator.constraints.Length;

import javax.persistence.*;

import javax.validation.constraints.Email;
import java.util.Collection;

@Entity
/**
 * Postgresql has user reserved as a keyword so we use account instead
 */
@Table(name = "account", schema = "public")
public class BrunchAccount extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "enabled")
    private boolean enabled;

    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Length(min = 1, max = 255)
    @Column(name = "username")
    private String firstName;

    @Length(max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @ManyToMany
    @JoinTable(
            name = "users_roles", schema = "public",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles;

    public Integer getId() {
        return id;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getPassword() {
        return password;
    }

    public Collection<Role> getRoles() {
        return roles;
    }
}

