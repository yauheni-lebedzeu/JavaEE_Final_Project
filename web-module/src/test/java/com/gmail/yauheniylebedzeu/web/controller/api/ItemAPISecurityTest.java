package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.ItemService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.service.model.ItemDTO;
import com.gmail.yauheniylebedzeu.web.configuration.TestUserDetailsConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_ITEM_QUANTITY_IN_STOCK;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_DESCRIPTION;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ITEM_NAME;
import static com.gmail.yauheniylebedzeu.web.constant.TestConstant.TEST_UUID;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = ItemAPIController.class)
@Import({BindingResultConverterImpl.class, TestUserDetailsConfig.class})
public class ItemAPISecurityTest {

    public static final String CUSTOMER_USER_ROLE_NAME = "CUSTOMER_USER";
    public static final String SALE_USER_ROLE_NAME = "SALE_USER";

    @MockBean
    private ItemService itemService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToGetItems() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToGetItems() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToGetItems() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToGetItems() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToGetItems() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToGetItem() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToGetItem() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToGetItem() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToGetItem() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToGetItem() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToDeleteItem() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToDeleteItem() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToDeleteItem() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToDeleteItem() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToDeleteItem() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToAddItem() throws Exception {
        ItemDTO item = new ItemDTO();
        item.setName(StringUtils.repeat("N", MIN_LENGTH_OF_ITEM_NAME));
        item.setDescription(StringUtils.repeat("D", MIN_LENGTH_OF_ITEM_DESCRIPTION));
        item.setPrice(BigDecimal.ONE);
        item.setQuantityInStock(MIN_ITEM_QUANTITY_IN_STOCK);
        mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(item))
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToAddItem() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToAddItem() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToAddUItem() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToAddItem() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ITEMS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
}