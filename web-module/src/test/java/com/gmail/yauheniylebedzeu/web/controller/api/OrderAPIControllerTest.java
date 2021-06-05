package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.OrderService;
import com.gmail.yauheniylebedzeu.service.enums.OrderStatusDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.web.constant.TestConstant.TEST_UUID;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ORDERS_CONTROLLER_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = OrderAPIController.class)
@ActiveProfiles(value = "test")
public class OrderAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldRequestGetOrders() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestGetOrders() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        );
        verify(orderService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyCollectionWhenWeRequestGetOrders() throws Exception {
        when(orderService.findAll()).thenReturn(Collections.emptyList());
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString)
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    void shouldReturnOrderCollectionWhenWeRequestGetOrders() throws Exception {
        OrderDTO order = getOrder();
        List<OrderDTO> orders = Collections.singletonList(order);
        when(orderService.findAll()).thenReturn(orders);
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString)
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(orders));
    }

    @Test
    void shouldRequestGetOrder() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestGetOrder() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        );
        verify(orderService, times(1)).findByUuid(TEST_UUID);
    }

    @Test
    void shouldReturnItemDTOWhenWeRequestGetItem() throws Exception {
        OrderDTO order = getOrder();
        when(orderService.findByUuid(TEST_UUID)).thenReturn(order);
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString)
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(order));
    }

    private OrderDTO getOrder() {
        OrderDTO order = new OrderDTO();
        order.setStatus(OrderStatusDTOEnum.NEW);
        String orderDateTime = formatDateTime(LocalDateTime.now());
        order.setOrderDateTime(orderDateTime);
        return order;
    }
}