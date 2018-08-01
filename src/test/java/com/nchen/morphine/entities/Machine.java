package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.*;

@Entity
public class Machine {
    @Id
    @Column
    int id;
    @Column
    String name;
    @Column(name = "description", nullable = false, length = 52)
    String description;
    @ManyToOne(joinColumn = "driver_id")
    Driver driver;
}
