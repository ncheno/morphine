package com.nchen.morphine.repository;

import java.util.Collection;

public interface Repository<T> {
    T create(T entity);
    T update(T entity);
    void delete(T entity);
    void deleteById(int id);
    T findById(int id);
    Collection<T> findAll();
}
