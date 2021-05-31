package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.service.converter.CartConverter;
import com.gmail.yauheniylebedzeu.service.converter.ItemConverter;
import com.gmail.yauheniylebedzeu.service.model.CartDTO;
import com.gmail.yauheniylebedzeu.service.model.CartDetailDTO;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getItem;

@Component
@AllArgsConstructor
public class CartConverterImpl implements CartConverter {

    private final ItemConverter itemConverter;

    @Override
    public CartDTO convertCartToCartDTO(Set<CartDetail> cart) {
        CartDTO cartDTO = new CartDTO();
        BigDecimal totalAmount = new BigDecimal("0");
        Integer totalQuantity = 0;
        for (CartDetail cartDetail : cart) {
            CartDetailDTO cartDetailDTO = convertCartDetailToCartDetailDTO(cartDetail);
            totalQuantity += cartDetailDTO.getQuantity();
            totalAmount = totalAmount.add(cartDetailDTO.getAmount());
            cartDTO.addCartDetail(cartDetailDTO);
        }
        cartDTO.setTotalQuantity(totalQuantity);
        cartDTO.setTotalAmount(totalAmount);
        return cartDTO;
    }

    @Override
    public CartDetailDTO convertCartDetailToCartDetailDTO(CartDetail cartDetail) {
        Item item = getItem(cartDetail);
        CartDetailDTO cartDetailDTO = new CartDetailDTO();
        Integer quantity = cartDetail.getQuantity();
        cartDetailDTO.setQuantity(quantity);
        BigDecimal itemPrice = item.getPrice();
        BigDecimal amount = itemPrice.multiply(BigDecimal.valueOf(quantity));
        cartDetailDTO.setAmount(amount);
        ItemDTO itemDTO = itemConverter.convertItemToItemDTO(item);
        cartDetailDTO.setItem(itemDTO);
        return cartDetailDTO;
    }
}