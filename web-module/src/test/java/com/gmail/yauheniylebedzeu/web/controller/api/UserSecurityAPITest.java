package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.web.configuration.TestUserDetailsConfig;
import com.gmail.yauheniylebedzeu.web.validator.UserValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = UserAPIController.class)
@Import({UserValidator.class, BindingResultConverterImpl.class, TestUserDetailsConfig.class})
public class UserSecurityAPITest {

    public static final String CUSTOMER_USER_ROLE_NAME = "CUSTOMER_USER";
    public static final String SALE_USER_ROLE_NAME = "SALE_USER";
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldUserWithRoleSecureRESTApiHasAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldUserWithWrongPasswordTryToGetAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUserWithAdminRoleTryToGetAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldUserWithCustomerUserRoleTryToGetAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldUserWithSaleUserRoleTryToGetAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
}
