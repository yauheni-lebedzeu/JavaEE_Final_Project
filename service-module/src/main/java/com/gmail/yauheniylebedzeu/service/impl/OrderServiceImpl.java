package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ItemRepository;
import com.gmail.yauheniylebedzeu.repository.OrderRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.enums.OrderStatusEnum;
import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.repository.model.OrderDetail;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.OrderService;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.OrderConverter;
import com.gmail.yauheniylebedzeu.service.exception.EmptyCartException;
import com.gmail.yauheniylebedzeu.service.exception.OrderNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.WrongItemQuantityException;
import com.gmail.yauheniylebedzeu.service.exception.WrongOrderStatusException;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getCart;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getItem;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.checkPageNumber;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final OrderConverter orderConverter;

    @Override
    @Transactional
    public OrderDTO makeOrder(String userUuid) {
        User user = userService.getSafeUser(userUuid);
        Set<CartDetail> cart = getCart(user);
        if (cart.isEmpty()) {
            throw new EmptyCartException(String.format("The user with uuid%s tried to place an order with an empty" +
                    " shopping cart!", userUuid));
        }
        for (CartDetail cartDetail : cart) {
            Integer quantityInCart = cartDetail.getQuantity();
            Item item = getItem(cartDetail);
            Integer quantityInStock = item.getQuantityInStock();
            if (quantityInCart > quantityInStock) {
                throw new WrongItemQuantityException(String.format("The order could not be formed. There are not so" +
                        " many  pieces of the item with id = %d in stock", item.getId()));
            }
            int newQuantityInStock = quantityInStock - quantityInCart;
            item.setQuantityInStock(newQuantityInStock);
            itemRepository.merge(item);
        }
        Order order = orderConverter.convertCartToOrder(cart);
        order.setUser(user);
        order.setStatus(OrderStatusEnum.NEW);
        orderRepository.persist(order);
        cart.clear();
        userRepository.merge(user);
        return orderConverter.convertOrderToDetailedOrderDTO(order);
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
    public OrderDTO findByUuid(String orderUuid) {
        Order order = getSafeOrder(orderUuid);
        return orderConverter.convertOrderToDetailedOrderDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO changeStatus(String orderUuid, String newStatus) {
        Order order = getSafeOrder(orderUuid);
        OrderStatusEnum status = order.getStatus();
        OrderStatusEnum orderNewStatusEnum = OrderStatusEnum.valueOf(newStatus);
        if (status.equals(OrderStatusEnum.REJECTED) || status.equals(OrderStatusEnum.DELIVERED)) {
            throw new WrongOrderStatusException(String.format("Attempt to assign an invalid status to an order with" +
                    " an id = %d", order.getId()));
        }
        order.setStatus(orderNewStatusEnum);
        orderRepository.merge(order);
        return orderConverter.convertOrderToOrderDTO(order);
    }

    @Override
    @Transactional
    public OrderDTO reject(String orderUuid) {
        Order order = getSafeOrder(orderUuid);
        Set<OrderDetail> orderDetails = order.getOrderDetails();
        for (OrderDetail orderDetail : orderDetails) {
            Item item = getItem(orderDetail);
            Integer quantityInStock = item.getQuantityInStock();
            Integer quantityInOrder = orderDetail.getQuantity();
            quantityInStock = quantityInStock + quantityInOrder;
            item.setQuantityInStock(quantityInStock);
            itemRepository.merge(item);
        }
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

    @Override
    @Transactional
    public List<OrderDTO> findAll() {
        List<Order> orders = orderRepository.findAll();
        return orderConverter.convertOrderListToOrderDTOList(orders);
    }
}