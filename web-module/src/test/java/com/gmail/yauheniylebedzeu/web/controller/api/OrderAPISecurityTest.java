package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.OrderService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.web.configuration.TestUserDetailsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.yauheniylebedzeu.web.constant.TestConstant.TEST_UUID;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ORDERS_CONTROLLER_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = OrderAPIController.class)
@Import({BindingResultConverterImpl.class, TestUserDetailsConfig.class})
public class OrderAPISecurityTest {

    public static final String CUSTOMER_USER_ROLE_NAME = "CUSTOMER_USER";
    public static final String SALE_USER_ROLE_NAME = "SALE_USER";

    @MockBean
    private OrderService orderService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToGetOrders() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToGetOrders() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToGetOrders() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToGetOrders() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToGetOrders() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToGetOrder() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToGetOrder() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToGetOrder() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToGetOrder() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToGetOrder() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ORDERS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
}
