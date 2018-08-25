package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.*;
import com.nchen.morphine.entity.EntityId;

import java.util.List;

@Entity
public class Driver extends EntityId{

    @Column
    public String name;

    @OneToMany(mappedBy = "driverList")
    public Machine machine;

    @OneToOne(joinColumn = "documents_id")
    public Documents documents;

    @ManyToMany
    @JoinTable(name = "driver_machine", joinColumn = "driver_id", inverseJoinColumn = "machine_id")
    List<Machine> machines;
}
