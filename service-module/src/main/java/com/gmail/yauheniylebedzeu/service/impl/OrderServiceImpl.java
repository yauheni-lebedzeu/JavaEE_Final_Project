package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.OrderRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.enums.OrderStatusEnum;
import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.OrderService;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.OrderConverter;
import com.gmail.yauheniylebedzeu.service.exception.OrderNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getCart;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.checkPageNumber;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final OrderConverter orderConverter;

    @Override
    @Transactional
    public void makeOrder(String userUuid) {
        User user = userService.getSafeUser(userUuid);
        Set<CartDetail> cart = getCart(user);
        Order order = orderConverter.convertCartToOrder(cart);
        order.setUser(user);
        orderRepository.persist(order);
        cart.clear();
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getUserOrdersPage(String userUuid, int pageNumber,
                                               int pageSize, String sortParameter) {
        PageDTO<OrderDTO> page = new PageDTO<>();
        Long countOfOrders = orderRepository.getCountOfUserOrders(userUuid);
        int countOfPages = getCountOfPages(countOfOrders, pageSize);
        page.setCountOfPages(countOfPages);
        pageNumber = checkPageNumber(pageNumber, countOfPages);
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Order> orders = orderRepository.findUserOrdersWithLimits(userUuid, startPosition, pageSize, sortParameter);
        List<OrderDTO> orderDTOs = orderConverter.convertOrderListToOrderDTOList(orders);
        page.addObjects(orderDTOs);
        return page;
    }

    @Override
    @Transactional
    public PageDTO<OrderDTO> getOrdersPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<OrderDTO> page = new PageDTO<>();
        Long countOfOrders = orderRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfOrders, pageSize);
        page.setCountOfPages(countOfPages);
        pageNumber = checkPageNumber(pageNumber, countOfPages);
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Order> orders = orderRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<OrderDTO> orderDTOs = orderConverter.convertOrderListToOrderDTOList(orders);
        page.addObjects(orderDTOs);
        return page;
    }

    @Override
    @Transactional
    public OrderDTO findOrderByUuid(String orderUuid) {
        Order order = getSafeOrder(orderUuid);
        return orderConverter.convertOrderToDetailedOrderDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO changeOrderStatus(String orderUuid, String newStatus) {
        Order order = getSafeOrder(orderUuid);
        OrderStatusEnum status = order.getStatus();
        OrderStatusEnum orderNewStatusEnum = OrderStatusEnum.valueOf(newStatus);
        if (status.equals(OrderStatusEnum.REJECTED) || status.equals(OrderStatusEnum.DELIVERED)) {
            throw new IllegalArgumentException("Invalid order status");
        }
        order.setStatus(orderNewStatusEnum);
        orderRepository.merge(order);
        return orderConverter.convertOrderToOrderDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO rejectOrder(String orderUuid) {
        Order order = getSafeOrder(orderUuid);
        order.setStatus(OrderStatusEnum.REJECTED);
        orderRepository.merge(order);
        return orderConverter.convertOrderToOrderDTO(order);
    }

    @Override
    @Transactional
    public Order getSafeOrder(String orderUuid) {
        Optional<Order> optionalOrder = orderRepository.findByUuid(orderUuid);
        if (optionalOrder.isPresent()) {
            return optionalOrder.get();
        } else {
            throw new OrderNotFoundException(String.format("Couldn't find the order with uuid = %s", orderUuid));
        }
    }
}