package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.Id;
import com.nchen.morphine.annotations.OneToOne;

@Entity
public class Documents {
    @Id
    @Column(name = "docId")
    int id;
    @Column
    String date;
    @OneToOne(mappedBy = "documents")
    Driver driver;
}
