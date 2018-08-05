package com.nchen.morphine.entity;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Id;

public abstract class EntityId {
    @Id
    @Column
    public int id;
}
