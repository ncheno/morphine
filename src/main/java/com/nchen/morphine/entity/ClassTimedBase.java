package com.nchen.morphine.entity;

import com.nchen.morphine.annotations.Column;

import java.util.Date;

public class ClassTimedBase extends ClassBase {

    @Column
    public Date createdAt;

    @Column
    public Date updatedAt;

    @Column
    public Date deletedAt;

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }
}
