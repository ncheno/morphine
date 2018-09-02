package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.*;
import com.nchen.morphine.entity.BaseEntity;

import java.util.List;

@Entity
public class Machine extends BaseEntity {

    @Column
    String name;

    @Column(name = "description", nullable = false, length = 52)
    String description;

    @ManyToOne(joinColumn = "driver_id")
    List<Driver> driverList;

    @ManyToMany(mappedBy = "machines")
    List<Driver> drivers;
}
