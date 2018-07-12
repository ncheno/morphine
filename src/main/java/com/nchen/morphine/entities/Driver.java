package com.nchen.morphine.entities;

import com.nchen.morphine.annotations.Entity;
import com.nchen.morphine.annotations.Id;
import com.nchen.morphine.annotations.OneToOne;

@Entity
public class Driver {
    @Id
    int id;
    String name;

    @OneToOne(mappedBy = "machine")
    Machine machine;
}
