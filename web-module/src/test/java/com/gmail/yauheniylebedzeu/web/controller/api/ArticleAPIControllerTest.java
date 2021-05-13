package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ArticleAPIController.class)
@ActiveProfiles("test")
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
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL).contentType(MediaType.APPLICATION_JSON)
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
        ArticleDTO article = new ArticleDTO();
        article.setId(1L);
        article.setUuid("518b860f-af7f-11eb-8627-0242ac130002");
        article.setAdditionDate(LocalDate.now());
        article.setTitle("test title");
        article.setContent("test content");
        article.setSynopsis("test synopsis");
        article.setUserFirstName("test user first name");
        article.setUserLastName("test user last name");
        List<ArticleDTO> articles = Collections.singletonList(article);
        when(articleService.findAll()).thenReturn(articles);
        MvcResult mvcResult = mockMvc.perform(
                get(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL).contentType(MediaType.APPLICATION_JSON)
        ).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(articles));
    }
}
