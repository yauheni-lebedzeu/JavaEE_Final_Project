package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.CartDTO;

public interface CartService {

    void addItemToCart(String itemUuid, String userUuid);

    CartDTO getUserCartByUserUuid(String userUuid);

    void updateItemQuantity(String itemUuid, int newQuantity, String userUuid);

    void clearCart(String userUuid);
}
