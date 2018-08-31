package com.nchen.morphine.entity;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Table;

@Table("ClassA")
public class ClassA extends ClassTimedBase {

    @Column
    public String name;

}
