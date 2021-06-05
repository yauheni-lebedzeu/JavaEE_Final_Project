package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
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

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_CONTENT;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_SYNOPSIS;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_TITLE;
import static com.gmail.yauheniylebedzeu.web.constant.TestConstant.TEST_UUID;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = ArticleAPIController.class)
@Import({BindingResultConverterImpl.class, TestUserDetailsConfig.class})
public class ArticleAPISecurityTest {

    public static final String CUSTOMER_USER_ROLE_NAME = "CUSTOMER_USER";
    public static final String SALE_USER_ROLE_NAME = "SALE_USER";

    @MockBean
    private ArticleService articleService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToGetArticles() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToGetArticles() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToGetArticles() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToGetArticles() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToGetArticles() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToGetArticle() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToGetArticle() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToGetArticle() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToGetArticle() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToGetArticle() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToDeleteArticle() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToDeleteArticle() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToDeleteArticle() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToDeleteArticle() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToDeleteArticle() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    void shouldVerifyThatUserWithRoleSecureRESTApiHasAccessToAddArticle() throws Exception {
        ArticleDTO article = new ArticleDTO();
        article.setTitle(StringUtils.repeat("T", MIN_LENGTH_OF_ARTICLE_TITLE));
        article.setSynopsis(StringUtils.repeat("S", MIN_LENGTH_OF_ARTICLE_SYNOPSIS));
        article.setContent(StringUtils.repeat("C", MIN_LENGTH_OF_ARTICLE_CONTENT));
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
                        .with(httpBasic("user", "1234"))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldVerifyThatUserWithWrongPasswordHasNoAccessToAddArticle() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("user", "5678"))
        ).andExpect(status().isUnauthorized());
    }

    @Test
    void shouldVerifyThatUserWithAdminRoleHasNoAccessToAddArticle() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .with(httpBasic("admin", "1234"))
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = CUSTOMER_USER_ROLE_NAME)
    void shouldVerifyThatUserWithCustomerUserRoleHasNoAccessToAddUArticle() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = SALE_USER_ROLE_NAME)
    void shouldVerifyThatUserWithSaleUserRoleHasNoAccessToAddArticle() throws Exception {
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        ).andExpect(status().isForbidden());
    }
}