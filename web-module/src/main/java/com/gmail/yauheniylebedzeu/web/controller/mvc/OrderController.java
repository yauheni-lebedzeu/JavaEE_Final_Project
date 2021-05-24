package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.OrderService;
import com.gmail.yauheniylebedzeu.service.enums.OrderStatusDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CHANGE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.MAKE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ORDERS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.SELLER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserUuid;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getUserPrincipal;

@Controller
@AllArgsConstructor
@Log4j2
public class OrderController {

    public static final String REJECT_CONTROLLER_URL = "/reject";
    private final OrderService orderService;

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + ORDERS_CONTROLLER_URL + MAKE_CONTROLLER_URL)
    public String makeOrder() {
        String userUuid = getLoggedUserUuid();
        orderService.makeOrder(userUuid);
        return "redirect:" + CUSTOMER_CONTROLLER_URL + ORDERS_CONTROLLER_URL;
    }

    @GetMapping(value = SELLER_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
    public String getOrders(@RequestParam(defaultValue = "1") int pageNumber,
                            @RequestParam(defaultValue = "10") int pageSize, Model model) {
        PageDTO<OrderDTO> page = orderService.getOrdersPage(pageNumber,
                pageSize, "orderDateTime desc");
        model.addAttribute("page", page);
        return "orders";
    }

    @GetMapping(value = CUSTOMER_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
    public String getUserOrders(@RequestParam(defaultValue = "1") int pageNumber,
                                @RequestParam(defaultValue = "10") int pageSize, Model model) {
        String userUuid = getLoggedUserUuid();
        PageDTO<OrderDTO> page = orderService.getUserOrdersPage(userUuid, pageNumber,
                pageSize, "orderDateTime desc");
        model.addAttribute("page", page);
        return "user-orders";
    }

    @GetMapping(value = SELLER_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/{orderUuid}")
    public String getOrder(@PathVariable String orderUuid, Model model) {
        OrderDTO order = orderService.findOrderByUuid(orderUuid);
        model.addAttribute("order", order);
        OrderStatusDTOEnum[] orderStatuses = OrderStatusDTOEnum.values();
        model.addAttribute("statuses", orderStatuses);
        return "order";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/{orderUuid}" + CHANGE_CONTROLLER_URL)
    public String changeOrderStatus(@PathVariable String orderUuid,
                                    @RequestParam String newStatus) {
        try {
            OrderStatusDTOEnum.valueOf(newStatus);
            if (!newStatus.equals("NEW")) {
                orderService.changeOrderStatus(orderUuid, newStatus);
            }
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException(String.format("A status named %s does not exist", newStatus), e);
        }
        return "redirect:" + SELLER_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + orderUuid;
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + ORDERS_CONTROLLER_URL + REJECT_CONTROLLER_URL + "/{orderUuid}")
    public String rejectOrder(@PathVariable String orderUuid) {
        orderService.rejectOrder(orderUuid);
        return "redirect:" + CUSTOMER_CONTROLLER_URL + ORDERS_CONTROLLER_URL;
    }
}