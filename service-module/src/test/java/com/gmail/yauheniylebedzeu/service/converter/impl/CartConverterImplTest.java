package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.service.model.CartDTO;
import com.gmail.yauheniylebedzeu.service.model.CartDetailDTO;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CartConverterImplTest {

    private final CartConverterImpl cartConverter;

    public CartConverterImplTest() {
        ItemConverterImpl itemConverter = new ItemConverterImpl();
        cartConverter = new CartConverterImpl(itemConverter);
    }

    @Test
    void shouldConvertCartToCartDTOAndGetNotNullObject() {
        Set<CartDetail> cart = new HashSet<>();
        CartDTO cartDTO = cartConverter.convertCartToCartDTO(cart);
        assertNotNull(cartDTO);
    }

    @Test
    void shouldConvertCartToCartDTOAndGetEmptyCart() {
        Set<CartDetail> cart = new HashSet<>();
        CartDTO cartDTO = cartConverter.convertCartToCartDTO(cart);
        assertTrue(cartDTO.getCartDetails().isEmpty());
    }

    @Test
    void shouldConvertCartToCartDTOAndGetNotEmptyCart() {
        Set<CartDetail> cart = new HashSet<>();
        cart.add(getTestCartDetailOne());
        CartDTO cartDTO = cartConverter.convertCartToCartDTO(cart);
        assertFalse(cartDTO.getCartDetails().isEmpty());
    }

    @Test
    void shouldConvertCartToCartDTOAndGetNotNullCartDetailDTO() {
        Set<CartDetail> cart = new HashSet<>();
        cart.add(getTestCartDetailOne());
        CartDTO cartDTO = cartConverter.convertCartToCartDTO(cart);
        assertNotNull(cartDTO.getCartDetails().get(0));
    }

    @Test
    void shouldConvertCartToCartDTOAndGetRightTotalAmount() {
        Set<CartDetail> cart = new HashSet<>();
        cart.add(getTestCartDetailOne());
        cart.add(getTestCartDetailTwo());
        CartDTO cartDTO = cartConverter.convertCartToCartDTO(cart);
        assertEquals(new BigDecimal("123.00"), cartDTO.getTotalAmount());
    }

    @Test
    void shouldConvertCartToCartDTOAndGetRightTotalQuantity() {
        Set<CartDetail> cart = new HashSet<>();
        cart.add(getTestCartDetailOne());
        cart.add(getTestCartDetailTwo());
        CartDTO cartDTO = cartConverter.convertCartToCartDTO(cart);
        assertEquals(7, cartDTO.getTotalQuantity());
    }

    @Test
    void shouldConvertCartDetailToCartDetailDTOAndGetNotNullObject() {
        CartDetail testCartDetailOne = getTestCartDetailOne();
        CartDetailDTO cartDetailDTO = cartConverter.convertCartDetailToCartDetailDTO(testCartDetailOne);
        assertNotNull(cartDetailDTO);
    }

    @Test
    void shouldConvertCartDetailToCartDetailDTOAndGetRightItemDTO() {
        CartDetail testCartDetailTwo = getTestCartDetailTwo();
        CartDetailDTO cartDetailDTO = cartConverter.convertCartDetailToCartDetailDTO(testCartDetailTwo);
        assertEquals(2L, cartDetailDTO.getItem().getUniqueNumber());
    }

    @Test
    void shouldConvertCartDetailToCartDetailDTOAndGetRightQuantity() {
        CartDetail testCartDetailOne = getTestCartDetailOne();
        CartDetailDTO cartDetailDTO = cartConverter.convertCartDetailToCartDetailDTO(testCartDetailOne);
        assertEquals(2, cartDetailDTO.getQuantity());
    }

    @Test
    void shouldConvertCartDetailToCartDetailDTOAndGetRightAmount() {
        CartDetail testCartDetailTwo = getTestCartDetailTwo();
        CartDetailDTO cartDetailDTO = cartConverter.convertCartDetailToCartDetailDTO(testCartDetailTwo);
        assertEquals(new BigDecimal("103.00"), cartDetailDTO.getAmount());
    }

    @Test
    void shouldConvertCartDetailToCartDetailDTOAndGetRightQuantityInStock() {
        CartDetail testCartDetailOne = getTestCartDetailOne();
        CartDetailDTO cartDetailDTO = cartConverter.convertCartDetailToCartDetailDTO(testCartDetailOne);
        assertEquals(testCartDetailOne.getItem().getQuantityInStock(), cartDetailDTO.getQuantityInStock());
    }

    private CartDetail getTestCartDetailOne() {
        CartDetail cartDetail = new CartDetail();
        Item itemOne = new Item();
        itemOne.setId(1L);
        itemOne.setPrice(new BigDecimal("10.00"));
        itemOne.setQuantityInStock(50);
        cartDetail.setItem(itemOne);
        cartDetail.setQuantity(2);
        return cartDetail;
    }

    private CartDetail getTestCartDetailTwo() {
        CartDetail cartDetail = new CartDetail();
        Item itemTwo = new Item();
        itemTwo.setId(2L);
        itemTwo.setPrice(new BigDecimal("20.60"));
        itemTwo.setQuantityInStock(34);
        cartDetail.setItem(itemTwo);
        cartDetail.setQuantity(5);
        return cartDetail;
    }
}