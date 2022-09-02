package com.example.brunch.dbmodel;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles", schema = "public")
public class RoleEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code", nullable = false)
    private String code;

    @ManyToMany(mappedBy = "roles")
    private Set<AccountEntity> accounts = new HashSet<>();

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
