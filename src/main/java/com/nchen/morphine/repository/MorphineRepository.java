package com.nchen.morphine.repository;

import com.nchen.morphine.Morphine;
import com.nchen.morphine.MorphineManager;
import com.nchen.morphine.entity.ClassBase;
import com.nchen.morphine.entity.EntityId;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class MorphineRepository<T extends ClassBase> implements Repository<T> {

    private Morphine morphine = MorphineManager.getMorphine();

    @Override
    public T create(T entity) {
        List<T> result = createList(Collections.singletonList(entity));
        return result.get(0);
    }

    @Override
    public List<T> createList(List<T> entity) {
        String tableName = MorphineUtils.getTableNameOrThrow(entity);
        Map<String, Object> argsForInsert = MorphineUtils.getFieldToValueMap(entity);

        String insertStatement ...
        for (T e: entity) {
            e.setId();
        }

        List<Integer> res = //

        return findByIdList(res);
    }
}


