package com.nchen.morphine.entity;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Id;

public abstract class EntityId {
    @Id
    @Column
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
