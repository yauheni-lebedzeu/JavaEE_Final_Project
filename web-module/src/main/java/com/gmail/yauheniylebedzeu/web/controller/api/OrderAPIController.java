package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.repository.model.Order;
import com.gmail.yauheniylebedzeu.service.OrderService;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ORDERS_CONTROLLER_URL;

@RestController
@RequestMapping(value = API_CONTROLLER_URL)
@AllArgsConstructor
public class OrderAPIController {

    private final OrderService orderService;

    @GetMapping(value = ORDERS_CONTROLLER_URL)
    public List<OrderDTO> getOrders() {
        return orderService.findAll();
    }

    @GetMapping(value = ORDERS_CONTROLLER_URL + "/{uuid}")
    public OrderDTO getOrder(@PathVariable String uuid) {
        return orderService.findByUuid(uuid);
    }
}
