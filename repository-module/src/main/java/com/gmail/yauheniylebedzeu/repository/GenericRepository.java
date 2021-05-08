package com.gmail.yauheniylebedzeu.repository;

import java.util.List;

public interface GenericRepository<T> {

    void persist(T entity);

    T findByUuid(String uuid);

    void remove(T entity);

    void merge(T entity);

    List<T> findEntitiesWithLimit(int startPosition, int maxResult, String sortParameter);

    Long getCountOfEntities();

    List<T> findAll();
}
