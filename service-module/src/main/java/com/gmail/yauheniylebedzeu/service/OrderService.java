package com.gmail.yauheniylebedzeu.service;


import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;


public interface OrderService {

    void makeOrder(String userUuid);

    PageDTO<OrderDTO> getUserOrdersPage(String userUuid, int pageNumber, int pageSize, String sortParameter);

    OrderDTO rejectOrder(String orderUuid);

    PageDTO<OrderDTO> getOrdersPage(int pageNumber, int pageSize, String sortParameter);

    OrderDTO findOrderByUuid(String orderUuid);

    OrderDTO changeOrderStatus(String orderUuid, String status);

    Order getSafeOrder(String orderUuid);
}