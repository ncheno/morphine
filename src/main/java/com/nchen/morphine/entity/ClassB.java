package com.nchen.morphine.entity;

import com.nchen.morphine.annotations.Column;
import com.nchen.morphine.annotations.Table;

@Table("ClassB")
public class ClassB extends ClassBase {

    @Column
    public int classAId;

}
