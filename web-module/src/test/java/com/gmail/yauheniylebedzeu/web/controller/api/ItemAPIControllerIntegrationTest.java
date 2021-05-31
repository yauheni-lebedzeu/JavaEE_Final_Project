package com.gmail.yauheniylebedzeu.web.controller.api;


import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.web.configuration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_ITEM_QUANTITY_IN_STOCK;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_NAME;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Sql(scripts = {"/scripts/clean-item.sql", "/scripts/init-item.sql"})
public class ItemAPIControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGetAllItems() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ItemDTO>> response = getItemListResponseEntity(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().size());
        assertEquals("Item1", response.getBody().get(0).getName());
        assertNull(response.getBody().get(1).getDescription());
        assertEquals(new BigDecimal("138.01"), response.getBody().get(2).getPrice());
        assertEquals(99, response.getBody().get(3).getQuantityInStock());
    }

    @Test
    void shouldGetItem() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ItemDTO>> responseOne = getItemListResponseEntity(request);
        String articleUuid = responseOne.getBody().get(1).getUuid();

        ResponseEntity<ItemDTO> response = getItemResponseEntity(request, articleUuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(articleUuid, response.getBody().getUuid());
        assertEquals("Item2", response.getBody().getName());
        assertEquals("Description2", response.getBody().getDescription());
        assertEquals(new BigDecimal("1000.00"), response.getBody().getPrice());
        assertEquals(87, response.getBody().getQuantityInStock());
    }

    @Test
    @Sql(scripts = "/scripts/clean-item.sql")
    void shouldAddArticle() {
        ItemDTO item = new ItemDTO();
        item.setName(StringUtils.repeat("N", MIN_LENGTH_OF_ITEM_NAME));
        item.setDescription(StringUtils.repeat("D", MIN_LENGTH_OF_ITEM_DESCRIPTION));
        item.setPrice(BigDecimal.TEN);
        item.setQuantityInStock(MIN_ITEM_QUANTITY_IN_STOCK);
        HttpEntity<ItemDTO> requestOne = new HttpEntity<>(item, new HttpHeaders());
        ResponseEntity<?> responseOne = testRestTemplate.exchange(
                API_CONTROLLER_URL + ITEMS_CONTROLLER_URL,
                HttpMethod.POST,
                requestOne,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.CREATED, responseOne.getStatusCode());

        HttpEntity<String> requestTwo = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ItemDTO>> responseTwo = getItemListResponseEntity(requestTwo);
        String itemUuid = responseTwo.getBody().get(0).getUuid();

        ResponseEntity<ItemDTO> responseThree = getItemResponseEntity(requestTwo, itemUuid);
        assertEquals(HttpStatus.OK, responseThree.getStatusCode());
        assertEquals(itemUuid, responseThree.getBody().getUuid());
        assertEquals(StringUtils.repeat("N", MIN_LENGTH_OF_ITEM_NAME), responseThree.getBody().getName());
        assertEquals(StringUtils.repeat("D", MIN_LENGTH_OF_ITEM_DESCRIPTION), responseThree.getBody().getDescription());
        assertEquals(new BigDecimal("10.00"), responseThree.getBody().getPrice());
        assertEquals(MIN_ITEM_QUANTITY_IN_STOCK, responseThree.getBody().getQuantityInStock());
        assertEquals(false, responseThree.getBody().getIsDeleted());
    }

    @Test
    void shouldDeleteItem() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());

        ResponseEntity<List<ItemDTO>> responseOne = getItemListResponseEntity(request);
        String itemUuid = responseOne.getBody().get(3).getUuid();

        ResponseEntity<?> responseTwo = testRestTemplate.exchange(
                API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + itemUuid,
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.OK, responseTwo.getStatusCode());

        ResponseEntity<List<ItemDTO>> responseThree = getItemListResponseEntity(request);
        assertEquals(4, responseThree.getBody().size());
        assertEquals(true, responseThree.getBody().get(3).getIsDeleted());
    }

    private ResponseEntity<List<ItemDTO>> getItemListResponseEntity(HttpEntity<String> request) {
        return testRestTemplate.exchange(
                API_CONTROLLER_URL + ITEMS_CONTROLLER_URL,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private ResponseEntity<ItemDTO> getItemResponseEntity(HttpEntity<String> request, String itemUuid) {
        return testRestTemplate.exchange(
                API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + itemUuid,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}