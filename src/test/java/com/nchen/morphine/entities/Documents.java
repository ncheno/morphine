package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.Column;

import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.OneToOne;
import com.nchen.morphine.entity.EntityId;

@Entity
public class Documents extends EntityId {
    @Column
    String date;

    @OneToOne(mappedBy = "documents")
    Driver driver;
}
