package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartDTO {

    private List<CartDetailDTO> cartDetails = new ArrayList<>();
    private BigDecimal totalAmount;
    private Integer totalQuantity;

    public void addCartDetail(CartDetailDTO cartDetail) {
        cartDetails.add(cartDetail);
    }
}
