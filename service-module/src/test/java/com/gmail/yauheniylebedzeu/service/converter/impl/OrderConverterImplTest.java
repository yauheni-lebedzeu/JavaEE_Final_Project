package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.enums.OrderStatusEnum;
import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.repository.model.OrderDetail;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.converter.OrderConverter;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFullName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderConverterImplTest {

    private final OrderConverter orderConverter;

    public OrderConverterImplTest() {
        orderConverter = new OrderConverterImpl();
    }

    @Test
    void shouldConvertCartToOrderAndGetNotNullObject() {
        Set<CartDetail> cart = new HashSet<>();
        Order order = orderConverter.convertCartToOrder(cart);
        assertNotNull(order);
    }

    @Test
    void shouldConvertCartToOrderAndGetRightOrderDetails() {
        Set<CartDetail> cart = new HashSet<>();
        CartDetail cartDetail = new CartDetail();
        cartDetail.setItem(new Item());
        cart.add(cartDetail);
        Order order = orderConverter.convertCartToOrder(cart);
        assertEquals(1, order.getOrderDetails().size());
    }

    @Test
    void shouldConvertOderToOrderDTOAndGetNotNullObject() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        OrderDTO orderDTO = orderConverter.convertOrderToOrderDTO(order);
        assertNotNull(orderDTO);
    }

    @Test
    void shouldConvertOderToOrderDTOAndGetRightOrderNumber() {
        Order order = new Order();
        long id = 1L;
        order.setId(id);
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        OrderDTO orderDTO = orderConverter.convertOrderToOrderDTO(order);
        assertEquals(id, orderDTO.getOrderNumber());
    }

    @Test
    void shouldConvertOderToOrderDTOAndGetRightOrderUuid() {
        Order order = new Order();
        String uuid = UUID.randomUUID().toString();
        order.setUuid(uuid);
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        OrderDTO orderDTO = orderConverter.convertOrderToOrderDTO(order);
        assertEquals(uuid, orderDTO.getUuid());
    }

    @Test
    void shouldConvertOderToOrderDTOAndGetRightOrderStatus() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        OrderStatusEnum orderStatus = OrderStatusEnum.NEW;
        order.setStatus(orderStatus);
        OrderDTO orderDTO = orderConverter.convertOrderToOrderDTO(order);
        assertEquals(orderStatus.name(), orderDTO.getStatus().name());
    }

    @Test
    void shouldConvertOderToOrderDTOAndGetRightOrderDateTime() {
        Order order = new Order();
        LocalDateTime dateTime = LocalDateTime.now();
        order.setOrderDateTime(dateTime);
        String formattedDateTime = formatDateTime(dateTime);
        order.setStatus(OrderStatusEnum.NEW);
        OrderDTO orderDTO = orderConverter.convertOrderToOrderDTO(order);
        assertEquals(formattedDateTime, orderDTO.getOrderDateTime());
    }

    @Test
    void shouldConvertOderToDetailedOrderDTOAndGetNotNullObject() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        User user = new User();
        user.setContacts(new UserContacts());
        order.setUser(user);
        OrderDTO orderDTO = orderConverter.convertOrderToDetailedOrderDTO(order);
        assertNotNull(orderDTO);
    }

    @Test
    void shouldConvertOderToDetailedOrderDTOAndGetRightCustomerEmail() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        User user = new User();
        String email = "email";
        user.setEmail(email);
        user.setContacts(new UserContacts());
        order.setUser(user);
        OrderDTO orderDTO = orderConverter.convertOrderToDetailedOrderDTO(order);
        assertEquals(email, orderDTO.getCustomerEmail());
    }

    @Test
    void shouldConvertOderToDetailedOrderDTOAndGetRightCustomerFullName() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setPatronymic("Patronymic");
        String customerFullName = getFullName(user);
        user.setContacts(new UserContacts());
        order.setUser(user);
        OrderDTO orderDTO = orderConverter.convertOrderToDetailedOrderDTO(order);
        assertEquals(customerFullName, orderDTO.getCustomerFullName());
    }

    @Test
    void shouldConvertOderToDetailedOrderDTOAndGetRightCustomerAddress() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        User user = new User();
        UserContacts contacts = new UserContacts();
        String address = "Address";
        contacts.setAddress(address);
        user.setContacts(contacts);
        order.setUser(user);
        OrderDTO orderDTO = orderConverter.convertOrderToDetailedOrderDTO(order);
        assertEquals(address, orderDTO.getCustomerAddress());
    }

    @Test
    void shouldConvertOderToDetailedOrderDTOAndGetRightCustomerPhoneNumber() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        User user = new User();
        UserContacts contacts = new UserContacts();
        String phoneNumber = "1111111111";
        contacts.setPhoneNumber(phoneNumber);
        user.setContacts(contacts);
        order.setUser(user);
        OrderDTO orderDTO = orderConverter.convertOrderToDetailedOrderDTO(order);
        assertEquals(phoneNumber, orderDTO.getCustomerPhoneNumber());
    }

    @Test
    void shouldConvertOderToDetailedOrderDTOAndGetRightIsCustomerDeleted() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        User user = new User();
        boolean isDeleted = false;
        user.setIsDeleted(isDeleted);
        user.setContacts(new UserContacts());
        order.setUser(user);
        OrderDTO orderDTO = orderConverter.convertOrderToDetailedOrderDTO(order);
        assertEquals(isDeleted, orderDTO.getIsCustomerDeleted());
    }

    @Test
    void shouldConvertEmptyOrderListToOrderDTOListAndGetEmptyList() {
        List<Order> orders = Collections.emptyList();
        List<OrderDTO> orderDTOs = orderConverter.convertOrderListToOrderDTOList(orders);
        assertTrue(orderDTOs.isEmpty());
    }

    @Test
    void shouldConvertOrderListToOrderDTOList() {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        order.setStatus(OrderStatusEnum.NEW);
        List<Order> orders = Collections.singletonList(order);
        List<OrderDTO> orderDTOs = orderConverter.convertOrderListToOrderDTOList(orders);
        assertEquals(1, orderDTOs.size());
    }
}