package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.web.constant.TestConstant.TEST_UUID;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = ArticleAPIController.class)
@ActiveProfiles(value = "test")
public class ArticleAPIControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArticleService articleService;

    @Test
    void shouldRequestGetArticles() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL).contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestGetArticles() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        verify(articleService, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyCollectionWhenWeRequestGetArticles() throws Exception {
        when(articleService.findAll()).thenReturn(Collections.emptyList());
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(Collections.emptyList()));
    }

    @Test
    void shouldReturnCollectionOfArticlesWhenWeRequestGetArticles() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        List<ArticleDTO> articles = Collections.singletonList(article);
        when(articleService.findAll()).thenReturn(articles);
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(articles));
    }

    @Test
    void shouldRequestGetArticle() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestGetArticle() throws Exception {
        mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
        );
        verify(articleService, times(1)).findByUuid(TEST_UUID);
    }

    @Test
    void shouldReturnArticleDTOWhenWeRequestGetArticle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        when(articleService.findByUuid(TEST_UUID)).thenReturn(article);
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString)
                .isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(article));
    }

    @Test
    void shouldRequestDeleteArticle() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk());
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestedDeleteArticle() throws Exception {
        mockMvc.perform(
                delete(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
        );
        verify(articleService, times(1)).removeByUuid(TEST_UUID);
    }

    private ArticleDTO getArticleDTOWithValidFields() {
        ArticleDTO article = new ArticleDTO();
        String formattedDateTime = formatDateTime(LocalDateTime.now());
        article.setAdditionDateTime(formattedDateTime);
        article.setTitle("test title");
        article.setContent("test content");
        article.setSynopsis("test synopsis");
        return article;
    }
}