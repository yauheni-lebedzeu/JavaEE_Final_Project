package com.gmail.yauheniylebedzeu.web.controller.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.converter.impl.BindingResultConverterImpl;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(excludeAutoConfiguration = UserDetailsServiceAutoConfiguration.class,
        controllers = ArticleAPIController.class)
@Import(BindingResultConverterImpl.class)
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

    @Test
    void shouldRequestAddArticle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        );
    }

    @Test
    void shouldVerifyThatBusinessLogicIsCalledWhenWeRequestedAddArticle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        );
        verify(articleService, times(1)).add(TEST_UUID, article);
    }

    @Test
    void shouldAddArticleWithValidFields() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isCreated());
    }

    @Test
    void shouldAddArticleWithNullTitle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setTitle(null);
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("title", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithEmptyTitle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setTitle("");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).contains("title", "This field cannot be empty or consist of only spaces!",
                "The article title must be between 3 and 50 characters long!");
    }

    @Test
    void shouldAddArticleWithOnlySpacesInTitle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setTitle("   ");
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("title", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithTooShortTitle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setTitle("Ti");
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The article title must be between 3 and 50 characters long!");
        errors.addError("title", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithTooLongTitle() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setTitle(StringUtils.repeat("T", 51));
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The article title must be between 3 and 50 characters long!");
        errors.addError("title", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithNullSynopsis() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setSynopsis(null);
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("synopsis", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithEmptySynopsis() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setSynopsis("");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).contains("synopsis", "This field cannot be empty or consist of only spaces!",
                "The article synopsis must be between 20 and 200 characters long!");
    }

    @Test
    void shouldAddArticleWithOnlySpacesInSynopsis() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setSynopsis(StringUtils.repeat(" ", 21));
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("synopsis", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithTooShortSynopsis() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setSynopsis("Synopsis");
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The article synopsis must be between 20 and 200 characters long!");
        errors.addError("synopsis", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithTooLongSynopsis() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setSynopsis(StringUtils.repeat("S", 201));
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The article synopsis must be between 20 and 200 characters long!");
        errors.addError("synopsis", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithNullContent() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setContent(null);
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("content", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithEmptyContent() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setContent("");
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).contains("content", "This field cannot be empty or consist of only spaces!",
                "The article text must be between 100 and 1000 characters long!");
    }

    @Test
    void shouldAddArticleWithOnlySpacesInContent() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setContent(StringUtils.repeat(" ", 101));
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("This field cannot be empty or consist of only spaces!");
        errors.addError("content", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithTooShortContent() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setContent("Content");
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The article text must be between 100 and 1000 characters long!");
        errors.addError("content", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    @Test
    void shouldAddArticleWithTooLongContent() throws Exception {
        ArticleDTO article = getArticleDTOWithValidFields();
        article.setContent(StringUtils.repeat("C", 1001));
        ErrorsDTO errors = new ErrorsDTO();
        ArrayList<String> errorMessages = new ArrayList<>();
        errorMessages.add("The article text must be between 100 and 1000 characters long!");
        errors.addError("content", errorMessages);
        MvcResult mvcResult = mockMvc.perform(
                post(API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + TEST_UUID)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(article))
        ).andExpect(status().isBadRequest()).andReturn();
        String contentAsString = mvcResult.getResponse().getContentAsString();
        assertThat(contentAsString).
                isEqualToIgnoringWhitespace(objectMapper.writeValueAsString(errors));
    }

    private ArticleDTO getArticleDTOWithValidFields() {
        ArticleDTO article = new ArticleDTO();
        article.setTitle("Title");
        article.setSynopsis("Test synopsis, test synopsis, test synopsis, test synopsis");
        article.setContent("Test content, test content, test content, test content, test content, test content," +
                " test content, test content, test content, test content, test content, test content, test content");
        return article;
    }
}