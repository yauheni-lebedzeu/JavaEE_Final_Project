package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.model.CartDetail;
import com.gmail.yauheniylebedzeu.repository.model.Item;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.CartService;
import com.gmail.yauheniylebedzeu.service.converter.CartConverter;
import com.gmail.yauheniylebedzeu.service.model.CartDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getCart;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getItem;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final ItemServiceImpl itemService;
    private final CartConverter cartConverter;

    @Override
    @Transactional
    public void addItemToCart(String itemUuid, String userUuid) {
        User user = userService.getSafeUser(userUuid);
        Set<CartDetail> cart = getCart(user);
        Item item = itemService.getSafeItem(itemUuid);
        Optional<CartDetail> optionalCartDetail = findCartDetailByItem(item, cart);
        if (optionalCartDetail.isPresent()) {
            CartDetail cartDetail = optionalCartDetail.get();
            cartDetail.incrementQuantity();
        } else {
            CartDetail cartDetail = new CartDetail(item, user);
            cart.add(cartDetail);
        }
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public CartDTO getUserCartByUserUuid(String userUuid) {
        User user = userService.getSafeUser(userUuid);
        Set<CartDetail> cart = getCart(user);
        return cartConverter.convertCartToCartDTO(cart);
    }

    @Override
    @Transactional
    public void updateItemQuantity(String itemUuid, int newQuantity, String userUuid) {
        User user = userService.getSafeUser(userUuid);
        Set<CartDetail> cart = getCart(user);
        Item item = itemService.getSafeItem(itemUuid);
        Optional<CartDetail> optionalCartDetail = findCartDetailByItem(item, cart);
        CartDetail cartDetail = optionalCartDetail.get();
        if (newQuantity == 0) {
            cart.remove(cartDetail);
        } else {
            cartDetail.setQuantity(newQuantity);
        }
        userRepository.merge(user);
    }

    @Override
    @Transactional
    public void clearCart(String userUuid) {
        User user = userService.getSafeUser(userUuid);
        Set<CartDetail> cart = getCart(user);
        cart.clear();
        userRepository.merge(user);
    }

    private Optional<CartDetail> findCartDetailByItem(Item item, Set<CartDetail> cart) {
        for (CartDetail cartDetail : cart) {
            Item itemFromCart = getItem(cartDetail);
            if (itemFromCart.equals(item)) {
                return Optional.of(cartDetail);
            }
        }
        return Optional.empty();
    }
}