package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.service.model.CartDTO;
import com.gmail.yauheniylebedzeu.service.model.CartDetailDTO;

import java.util.Set;

public interface CartConverter {

    CartDTO convertCartToCartDTO(Set<CartDetail> cart);

    CartDetailDTO convertCartDetailToCartDetailDTO(CartDetail cartDetail);
}