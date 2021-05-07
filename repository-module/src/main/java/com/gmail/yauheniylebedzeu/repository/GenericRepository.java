package com.gmail.yauheniylebedzeu.repository;

import java.util.List;

public interface GenericRepository<I, T> {

    void persist(T entity);

    T findById(I id);

    void remove(T entity);

    void merge(T entity);

    List<T> findAll(int startPosition, int maxResult, String sortParameter);

    I getCountOfEntities();
}
