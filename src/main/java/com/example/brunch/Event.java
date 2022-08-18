package com.example.brunch;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Event {

    @Id
    private long id;
    private String name;
}
