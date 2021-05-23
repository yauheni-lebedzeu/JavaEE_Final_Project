package com.gmail.yauheniylebedzeu.repository;

import com.gmail.yauheniylebedzeu.repository.model.Order;

import java.util.List;

public interface OrderRepository extends GenericRepository<Long, Order> {

    List<Order> findUserOrdersWithLimits(String userUuid, int startPosition, int maxResult, String sortParameter);

    Long getCountOfUserOrders(String userUuid);
}
