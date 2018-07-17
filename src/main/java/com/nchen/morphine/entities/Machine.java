package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.*;

@Entity
public class Machine {
    @Id
    int id;

    String name;

    @Column(name = "description", nullable = false, length = 52)
    String description;
}
