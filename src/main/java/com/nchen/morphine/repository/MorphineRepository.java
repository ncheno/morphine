package com.nchen.morphine.repository;

import com.nchen.morphine.Morphine;
import com.nchen.morphine.MorphineManager;
import com.nchen.morphine.entity.BaseEntity;

public abstract class MorphineRepository<T extends BaseEntity> implements Repository<T> {

    private Morphine morphine = MorphineManager.getMorphine();

    @Override
    public T create(T entity) {
        return null;
    }
}
