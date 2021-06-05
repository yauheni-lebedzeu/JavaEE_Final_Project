package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.web.configuration.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserAPIControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    @Sql(scripts = {"/scripts/clean-order.sql", "/scripts/clean-article.sql", "/scripts/clean-user.sql", "/scripts/init-user.sql"})
    void shouldGetAllUsers() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<UserDTO>> response = testRestTemplate.exchange(
                API_CONTROLLER_URL + USERS_CONTROLLER_URL,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().size());
        assertEquals("Admin@email.com", response.getBody().get(0).getEmail());
        assertEquals("Seller@email.com", response.getBody().get(1).getEmail());
        assertEquals("Customer@email.com", response.getBody().get(2).getEmail());
        assertEquals("Rest@email.com", response.getBody().get(3).getEmail());
    }

    @Test
    @Sql(value = {"/scripts/clean-order.sql", "/scripts/clean-article.sql", "/scripts/clean-user.sql"})
    void shouldAddUser() {
        UserDTO user = new UserDTO();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setPatronymic("Patronymic");
        user.setEmail("Admin@email.com");
        user.setPassword("Admin1234!");
        user.setRole(RoleDTOEnum.ADMIN);
        HttpEntity<UserDTO> request = new HttpEntity<>(user, new HttpHeaders());
        ResponseEntity<?> response = testRestTemplate.exchange(
                API_CONTROLLER_URL + USERS_CONTROLLER_URL,
                HttpMethod.POST,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }
}