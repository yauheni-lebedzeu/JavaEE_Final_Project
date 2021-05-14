package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.GenericRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public abstract class GenericRepositoryImpl<I, T> implements GenericRepository<I, T> {

    @PersistenceContext
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    @SuppressWarnings("unchecked")
    public GenericRepositoryImpl() {
        ParameterizedType genericClass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<T>) genericClass.getActualTypeArguments()[1];
    }

    @Override
    public void persist(T entity) {
        entityManager.persist(entity);
    }

    @Override
    public void remove(T entity) {
        entityManager.remove(entity);
    }

    @Override
    public void merge(T entity) {
        entityManager.merge(entity);
    }

    @Override
    public T findById(I id) {
        return entityManager.find(entityClass, id);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Optional<T> findByUuid(String uuid) {
        String queryString = "select c from " + entityClass.getName() + " c where c.uuid=:uuid";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("uuid", uuid);
        try {
            T object = (T) query.getSingleResult();
            return Optional.of(object);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findAll() {
        String queryString = "from " + entityClass.getName();
        Query query = entityManager.createQuery(queryString);
        return query.getResultList();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<T> findEntitiesWithLimits(int startPosition, int maxResult, String sortParameter) {
        String queryString = "select c from " + entityClass.getName() + " c order by c." + sortParameter;
        Query query = entityManager.createQuery(queryString);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    @Override
    public Long getCountOfEntities() {
        String queryString = "select count(c.id) from " + entityClass.getName() + " c";
        Query query = entityManager.createQuery(queryString);
        return (Long) query.getSingleResult();
    }
}