package com.gmail.yauheniylebedzeu.service;


import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;

import java.util.List;


public interface OrderService {

    OrderDTO makeOrder(String userUuid);

    PageDTO<OrderDTO> getUserOrdersPage(String userUuid, int pageNumber, int pageSize, String sortParameter);

    OrderDTO reject(String orderUuid);

    PageDTO<OrderDTO> getOrdersPage(int pageNumber, int pageSize, String sortParameter);

    OrderDTO findByUuid(String orderUuid);

    OrderDTO changeStatus(String orderUuid, String status);

    Order getSafeOrder(String orderUuid);

    List<OrderDTO> findAll();
}