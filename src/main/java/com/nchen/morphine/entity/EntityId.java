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



A a,b,c,d
B e,g,f

        INNER JOIN =>  A.a, B.e  => RS

        List<ClassE> result = getList(SQL,[] args, ClassE.class)
        Optional<ClassE> result = getOptionalOne(SQL,[] args, ClassE.class)


ClassE
  a
  b

List<ClassE>
Optional<ClassE>

