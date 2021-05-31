package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.model.OrderDTO;
import com.gmail.yauheniylebedzeu.web.configuration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.math.BigDecimal;
import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ORDERS_CONTROLLER_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Sql(scripts = {"/scripts/clean-article.sql", "/scripts/clean-order.sql",
        "/scripts/clean-item.sql", "/scripts/clean-user.sql", "/scripts/init-user.sql",
        "/scripts/init-item.sql", "/scripts/init-order.sql"})
public class OrderApiControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGetAllOrders() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<OrderDTO>> response = getOrderListResponseEntity(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(2, response.getBody().get(0).getOrderDetails().size());
        assertEquals("NEW", response.getBody().get(0).getStatus().name());
    }

    @Test
    void shouldGetArticle() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<OrderDTO>> responseOne = getOrderListResponseEntity(request);
        String orderUuid = responseOne.getBody().get(0).getUuid();

        ResponseEntity<OrderDTO> response = getOrderResponseEntity(request, orderUuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().getOrderDetails().size());
        assertEquals("NEW", response.getBody().getStatus().name());
        assertEquals("LastName3 FirstName3 Patronymic3", response.getBody().getCustomerFullName());
        assertEquals("Address", response.getBody().getCustomerAddress());
        assertEquals("+3751111111", response.getBody().getCustomerPhoneNumber());
    }

    private ResponseEntity<OrderDTO> getOrderResponseEntity(HttpEntity<String> request, String orderUuid) {
        return testRestTemplate.exchange(
                API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + orderUuid,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private ResponseEntity<List<OrderDTO>> getOrderListResponseEntity(HttpEntity<String> request) {
        return testRestTemplate.exchange(
                API_CONTROLLER_URL + ORDERS_CONTROLLER_URL,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}