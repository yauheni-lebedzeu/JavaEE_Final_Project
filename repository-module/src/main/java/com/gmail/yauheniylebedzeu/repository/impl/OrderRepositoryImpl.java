package com.gmail.yauheniylebedzeu.repository.impl;

import com.gmail.yauheniylebedzeu.repository.OrderRepository;
import com.gmail.yauheniylebedzeu.repository.model.Order;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.List;

@Repository
public class OrderRepositoryImpl extends GenericRepositoryImpl<Long, Order> implements OrderRepository {

    @Override
    @SuppressWarnings("unchecked")
    public List<Order> findUserOrdersWithLimits(String userUuid, int startPosition,
                                                int maxResult, String sortParameter) {
        String queryString = "select o from Order as o where o.user.uuid=:userUuid order by o." + sortParameter;
        Query query = entityManager.createQuery(queryString);
        query.setParameter("userUuid", userUuid);
        query.setFirstResult(startPosition);
        query.setMaxResults(maxResult);
        return query.getResultList();
    }

    @Override
    public Long getCountOfUserOrders(String userUuid) {
        String queryString = "select count(o.id) from Order as o where o.user.uuid=:userUuid";
        Query query = entityManager.createQuery(queryString);
        query.setParameter("userUuid", userUuid);
        return (Long) query.getSingleResult();
    }
}