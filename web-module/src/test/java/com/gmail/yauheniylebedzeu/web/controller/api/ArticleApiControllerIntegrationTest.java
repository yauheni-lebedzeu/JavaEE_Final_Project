package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
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
import org.testcontainers.shaded.org.apache.commons.lang.StringUtils;

import java.util.List;

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_CONTENT;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_SYNOPSIS;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_TITLE;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@Sql(scripts = {"/scripts/clean-article.sql","/scripts/clean-user.sql", "/scripts/init-user.sql", "/scripts/init-article.sql"})
public class ArticleApiControllerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldGetAllArticles() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ArticleDTO>> response = getArticleListResponseEntity(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(4, response.getBody().size());
        assertEquals("Title1", response.getBody().get(0).getTitle());
        assertEquals("Synopsis2", response.getBody().get(1).getSynopsis());
        assertNull(response.getBody().get(2).getContent());
        assertEquals("FirstName4 LastName4", response.getBody().get(3).getFirstAndLastName());
    }

    @Test
    void shouldGetArticle() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<ArticleDTO>> responseOne = getArticleListResponseEntity(request);
        String articleUuid = responseOne.getBody().get(2).getUuid();

        ResponseEntity<ArticleDTO> response = getArticleResponseEntity(request, articleUuid);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(articleUuid, response.getBody().getUuid());
        assertEquals("Title3", response.getBody().getTitle());
        assertEquals("Synopsis3", response.getBody().getSynopsis());
        assertEquals("Content3 Content3 Content3 Content3 Content3 Content3 Content3 Content3",
                response.getBody().getContent());
        assertEquals("FirstName3 LastName3", response.getBody().getFirstAndLastName());
        assertEquals(false, response.getBody().getIsAuthorDeleted());
    }

    @Test
    @Sql(scripts = {"/scripts/clean-article.sql","/scripts/clean-user.sql", "/scripts/init-user.sql"})
    void shouldAddArticle() {
        HttpEntity<String> requestOne = new HttpEntity<>(null, new HttpHeaders());
        ResponseEntity<List<UserDTO>> responseOne = testRestTemplate.exchange(
                API_CONTROLLER_URL + USERS_CONTROLLER_URL,
                HttpMethod.GET,
                requestOne,
                new ParameterizedTypeReference<>() {
                }
        );
        String userUuid = responseOne.getBody().get(1).getUuid();

        ArticleDTO article = new ArticleDTO();
        article.setTitle(StringUtils.repeat("T", MIN_LENGTH_OF_ARTICLE_TITLE));
        article.setSynopsis(StringUtils.repeat("S", MIN_LENGTH_OF_ARTICLE_SYNOPSIS));
        article.setContent(StringUtils.repeat("C", MIN_LENGTH_OF_ARTICLE_CONTENT));
        HttpEntity<ArticleDTO> requestTwo = new HttpEntity<>(article, new HttpHeaders());
        ResponseEntity<?> responseTwo = testRestTemplate.exchange(
                API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + userUuid,
                HttpMethod.POST,
                requestTwo,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.CREATED, responseTwo.getStatusCode());

        ResponseEntity<List<ArticleDTO>> responseThree = getArticleListResponseEntity(requestOne);
        String articleUuid = responseThree.getBody().get(0).getUuid();

        ResponseEntity<ArticleDTO> responseFour = getArticleResponseEntity(requestOne, articleUuid);
        assertEquals(HttpStatus.OK, responseFour.getStatusCode());
        assertEquals(articleUuid, responseFour.getBody().getUuid());
        assertEquals(StringUtils.repeat("T", MIN_LENGTH_OF_ARTICLE_TITLE), responseFour.getBody().getTitle());
        assertEquals(StringUtils.repeat("S", MIN_LENGTH_OF_ARTICLE_SYNOPSIS), responseFour.getBody().getSynopsis());
        assertEquals(StringUtils.repeat("C", MIN_LENGTH_OF_ARTICLE_CONTENT), responseFour.getBody().getContent());
        assertEquals("FirstName2 LastName2", responseFour.getBody().getFirstAndLastName());
        assertEquals(false, responseFour.getBody().getIsAuthorDeleted());
    }

    @Test
    void shouldDeleteArticle() {
        HttpEntity<String> request = new HttpEntity<>(null, new HttpHeaders());

        ResponseEntity<List<ArticleDTO>> responseOne = getArticleListResponseEntity(request);
        String articleUuid = responseOne.getBody().get(2).getUuid();

        ResponseEntity<?> responseTwo = testRestTemplate.exchange(
                API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + articleUuid,
                HttpMethod.DELETE,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
        assertEquals(HttpStatus.OK, responseTwo.getStatusCode());

        ResponseEntity<List<ArticleDTO>> responseThree = getArticleListResponseEntity(request);
        assertEquals(3, responseThree.getBody().size());
    }

    private ResponseEntity<List<ArticleDTO>> getArticleListResponseEntity(HttpEntity<String> request) {
        return testRestTemplate.exchange(
                API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
    }

    private ResponseEntity<ArticleDTO> getArticleResponseEntity(HttpEntity<String> request, String articleUuid) {
        return testRestTemplate.exchange(
                API_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/" + articleUuid,
                HttpMethod.GET,
                request,
                new ParameterizedTypeReference<>() {
                }
        );
    }
}