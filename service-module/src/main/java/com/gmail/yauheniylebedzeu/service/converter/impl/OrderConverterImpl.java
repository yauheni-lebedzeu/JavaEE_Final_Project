package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.enums.OrderStatusEnum;
import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.repository.model.OrderDetail;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.converter.OrderConverter;
import com.gmail.yauheniylebedzeu.service.enums.OrderStatusDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import com.gmail.yauheniylebedzeu.service.model.OrderDetailDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getItem;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getOrderDetails;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUser;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUserContacts;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFullName;

@Component
@AllArgsConstructor
public class OrderConverterImpl implements OrderConverter {

    @Override
    public Order convertCartToOrder(Set<CartDetail> cart) {
        Order order = new Order();
        order.setOrderDateTime(LocalDateTime.now());
        for (CartDetail cartDetail : cart) {
            Item item = getItem(cartDetail);
            OrderDetail orderDetail = new OrderDetail();
            orderDetail.setItem(item);
            Integer quantity = cartDetail.getQuantity();
            orderDetail.setQuantity(quantity);
            BigDecimal price = item.getPrice();
            orderDetail.setPrice(price);
            orderDetail.setOrder(order);
            order.addOrderDetail(orderDetail);
        }
        return order;
    }

    @Override
    public OrderDTO convertOrderToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderNumber(order.getId());
        orderDTO.setUuid(order.getUuid());

        LocalDateTime orderDateTime = order.getOrderDateTime();
        String formattedDateTime = formatDateTime(orderDateTime);
        orderDTO.setOrderDateTime(formattedDateTime);

        OrderStatusEnum orderStatusEnum = order.getStatus();
        String statusName = orderStatusEnum.name();
        OrderStatusDTOEnum orderStatusDTOEnum = OrderStatusDTOEnum.valueOf(statusName);
        orderDTO.setStatus(orderStatusDTOEnum);

        int totalQuantity = 0;
        BigDecimal totalAmount = new BigDecimal("0");
        Set<OrderDetail> orderDetails = getOrderDetails(order);
        for (OrderDetail orderDetail : orderDetails) {
            OrderDetailDTO orderDetailDTO = covertOrderDetailToOrderDetailDTO(orderDetail);
            totalQuantity += orderDetailDTO.getQuantity();
            totalAmount = totalAmount.add(orderDetailDTO.getAmount());
            orderDTO.addOrderDetail(orderDetailDTO);
        }
        orderDTO.setTotalQuantity(totalQuantity);
        orderDTO.setTotalAmount(totalAmount);
        return orderDTO;
    }

    @Override
    public OrderDTO convertOrderToDetailedOrderDTO(Order order) {
        User user = getUser(order);
        UserContacts contacts = getUserContacts(user);
        OrderDTO orderDTO = convertOrderToOrderDTO(order);
        orderDTO.setCustomerAddress(contacts.getAddress());
        orderDTO.setCustomerPhoneNumber(contacts.getPhoneNumber());
        orderDTO.setCustomerEmail(user.getEmail());
        String fullName = getFullName(user);
        orderDTO.setCustomerFullName(fullName);
        return orderDTO;
    }

    @Override
    public List<OrderDTO> convertOrderListToOrderDTOList(List<Order> orders) {
        return orders.stream()
                .map(this::convertOrderToOrderDTO)
                .collect(Collectors.toList());

    }

    private OrderDetailDTO covertOrderDetailToOrderDetailDTO(OrderDetail orderDetail) {
        Item item = getItem(orderDetail);
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setItemUuid(item.getUuid());
        orderDetailDTO.setItemName(item.getName());
        int quantity = orderDetail.getQuantity();
        orderDetailDTO.setQuantity(quantity);
        BigDecimal price = orderDetail.getPrice();
        orderDetailDTO.setPrice(price);
        BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
        orderDetailDTO.setAmount(amount);
        return orderDetailDTO;
    }
}