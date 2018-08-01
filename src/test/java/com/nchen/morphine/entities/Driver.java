package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.*;

@Entity
public class Driver {
    @Id
    @Column(name = "dId")
    public int id;
    @Column
    public String name;
    @OneToMany(mappedBy = "driver")
    public Machine machine;
    @OneToOne(joinColumn = "documents_id")
    public Documents documents;
}
