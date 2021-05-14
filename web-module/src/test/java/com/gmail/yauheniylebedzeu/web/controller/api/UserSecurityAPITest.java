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
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = UserAPIController.class)
@Import({UserValidator.class, BindingResultConverterImpl.class, TestUserDetailsConfig.class})
public class UserSecurityAPITest {

    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldUserWithRoleSecureRESTApiHasAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldUserWithWrongPasswordTryToGetAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldUserWithAdminRoleTryToGetAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"CUSTOMER_USER"})
    void shouldUserWithCustomerRoleTryToGetAccessToGetUsers() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + USERS_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
}
