package com.nchen.morphine.repository;

import com.nchen.morphine.entity.ClassBase;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Repository {
    static <T extends ClassBase> T create(T entity) {
        return null;
    }

    static <T extends ClassBase> List<T> create(List<T> entity) {
        return null;
    }

    static <T extends ClassBase> T update(T entity) {
        return null;
    }

    static <T extends ClassBase> List<T> update(List<T> entity) {
        return null;
    }

    static <T extends ClassBase> boolean delete(T entity) {
        return false;
    }


    static <T extends ClassBase> List<Boolean> delete(List<T> entity) {
        return null;
    }

// TODO     void deleteById(int id, String tableName);
// TODO     void deleteByIdList(List<Integer> ids, String tableName);

    static <T extends ClassBase> T findById(int id, Class<T> cls) {
        return null;
    }


    // TODO T findByIdList(List<Integer> ids);
    // TODO  findOptionalOne("SELECT name FROM xxx LIMIT 1", null, String.class)
    static <E> Optional<E> findOptionalOne(String sql, Object[] args, Class<E> cls) {
        isPrimitive() Integer String Boolean ...

        return null;
    }

    static <E> Optional<E> findOptionalOne(String sql, Class<E> cls) {
        return findOptionalOne(sql, null, cls);
    }

    static <E> Collection<E> find(String sql, Object[] args, Class<E> cls) {
        return null;
    }

    Collection<T> findAll();
}
