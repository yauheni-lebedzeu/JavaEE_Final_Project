package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.CartService;
import com.gmail.yauheniylebedzeu.service.model.CartDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADD_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CART_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CLEAR_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.UPDATE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getUserPrincipal;

@Controller
@AllArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL)
    public String getCart(Model model) {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        UserDTO user = optionalUser.get();
        String userUuid = user.getUuid();
        CartDTO cart = cartService.getUserCartByUserUuid(userUuid);
        model.addAttribute("cart", cart);
        return "cart";
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL + ADD_CONTROLLER_URL
            + "/{itemUuid}/{sourcePageNumber}")
    public String addItemToCart(@PathVariable String itemUuid,
                                @PathVariable String sourcePageNumber) {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        UserDTO user = optionalUser.get();
        String userUuid = user.getUuid();
        cartService.addItemToCart(itemUuid, userUuid);
        return "redirect:" + ITEMS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL + UPDATE_CONTROLLER_URL + "/{itemUuid}")
    public String updateQuantity(@PathVariable String itemUuid,
                                 @RequestParam int newQuantity) {
        if (newQuantity >= 0 && newQuantity <= 99) {
            Optional<UserDTO> optionalUser = getUserPrincipal();
            UserDTO user = optionalUser.get();
            String userUuid = user.getUuid();
            cartService.updateItemQuantity(itemUuid, newQuantity, userUuid);
        }
        return "redirect:" + CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL;
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL + CLEAR_CONTROLLER_URL)
    public String clearCart() {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        UserDTO user = optionalUser.get();
        String userUuid = user.getUuid();
        cartService.clearCart(userUuid);
        return "redirect:" + CUSTOMER_CONTROLLER_URL + CART_CONTROLLER_URL;
    }
}