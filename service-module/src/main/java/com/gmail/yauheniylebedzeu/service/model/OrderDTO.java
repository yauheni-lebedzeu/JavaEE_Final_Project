package com.gmail.yauheniylebedzeu.service.model;

import com.gmail.yauheniylebedzeu.service.enums.OrderStatusDTOEnum;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class OrderDTO {

    private Long orderNumber;
    private String uuid;
    private String orderDateTime;
    private OrderStatusDTOEnum status;
    private List<OrderDetailDTO> orderDetails = new ArrayList<>();
    private Integer totalQuantity;
    private BigDecimal totalAmount;

    private String customerFullName;
    private String customerEmail;
    private String customerAddress;
    private String customerPhoneNumber;

    public void addOrderDetail(OrderDetailDTO orderDetail) {
        orderDetails.add(orderDetail);
    }
}