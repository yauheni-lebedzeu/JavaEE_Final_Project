package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderDetailDTO {

    private String itemUuid;
    private String itemName;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal amount;
}
