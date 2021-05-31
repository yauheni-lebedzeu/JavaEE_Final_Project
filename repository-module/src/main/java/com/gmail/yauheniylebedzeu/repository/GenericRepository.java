package com.gmail.yauheniylebedzeu.repository;

import java.util.List;
import java.util.Optional;

public interface GenericRepository<I, T> {

    void persist(T entity);

    T findById(I id);

    Optional<T> findByUuid(String uuid);

    void remove(T entity);

    void merge(T entity);

    List<T> findEntitiesWithLimits(int startPosition, int maxResult, String sortParameter);

    Long getCountOfEntities();

    List<T> findAll();
}