package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;

import java.util.List;
import java.util.Set;

public interface OrderConverter {

    Order convertCartToOrder(Set<CartDetail> cart);

    OrderDTO convertOrderToOrderDTO(Order order);

    OrderDTO convertOrderToDetailedOrderDTO(Order order);

    List<OrderDTO> convertOrderListToOrderDTOList(List<Order> orders);
}