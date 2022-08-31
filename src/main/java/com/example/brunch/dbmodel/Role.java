package com.example.brunch.dbmodel;

import javax.persistence.*;

@Entity
@Table(name = "roles", schema = "public")
public class Role extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "code", nullable = false)
    private String code;

    public Integer getId() {
        return id;
    }

    public String getCode() {
        return code;
    }
}
