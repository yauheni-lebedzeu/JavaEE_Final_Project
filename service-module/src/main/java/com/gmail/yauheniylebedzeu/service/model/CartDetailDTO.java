package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CartDetailDTO {

    private ItemDTO item;
    private Integer quantity;
    private BigDecimal amount;
    private Integer quantityInStock;
}