package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.CartService;
import com.gmail.yauheniylebedzeu.service.model.CartDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADD_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CART_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CLEAR_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.UPDATE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserUuid;

@Controller
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL)
    public String getCart(Model model) {
        String userUuid = getLoggedUserUuid();
        CartDTO cart = cartService.getUserCartByUserUuid(userUuid);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL + ADD_CONTROLLER_URL
            + "/{itemUuid}/{sourcePageNumber}")
    public String addItemToCart(@PathVariable String itemUuid,
                                @PathVariable String sourcePageNumber) {
        String userUuid = getLoggedUserUuid();
        cartService.addItemToCart(itemUuid, userUuid);
        return "redirect:" + ITEMS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL + UPDATE_CONTROLLER_URL + "/{itemUuid}")
    public String updateQuantity(@PathVariable String itemUuid,
                                 @RequestParam int newQuantity) {
        if (newQuantity >= 0 && newQuantity <= 99) {
            String userUuid = getLoggedUserUuid();
            cartService.updateItemQuantity(itemUuid, newQuantity, userUuid);
        }
        return "redirect:" + CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL;
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL + CLEAR_CONTROLLER_URL)
    public String clearCart() {
        String userUuid = getLoggedUserUuid();
        cartService.clearCart(userUuid);
        return "redirect:" + CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL;
    }
}