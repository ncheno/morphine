package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.Column;

import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.OneToOne;
import com.nchen.morphine.entity.BaseEntity;

@Entity
public class Documents extends BaseEntity {
    @Column
    String date;

    @OneToOne(mappedBy = "documents")
    Driver driver;
}
