package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.ArticleContent;
import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.ArticleConverter;
import com.gmail.yauheniylebedzeu.service.converter.CommentConverter;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFirstAndLastNameString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ArticleConverterImplTest {

    private final ArticleConverter articleConverter;

    public ArticleConverterImplTest() {
        CommentConverter commentConverter = new CommentConverterImpl();
        this.articleConverter = new ArticleConverterImpl(commentConverter);
    }

    @Test
    void shouldConvertArticleDTOToArticleAndReturnNotNullObject() {
        ArticleDTO articleDTO = new ArticleDTO();
        Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
        assertNotNull(article);
    }

    @Test
    void shouldConvertArticleDTOToArticleAndReturnRightTitle() {
        ArticleDTO articleDTO = new ArticleDTO();
        String title = "Title";
        articleDTO.setTitle(title);
        Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
        assertEquals(title, article.getTitle());
    }

    @Test
    void shouldConvertArticleDTOToArticleAndReturnRightSynopsis() {
        ArticleDTO articleDTO = new ArticleDTO();
        String synopsis = "Synopsis";
        articleDTO.setSynopsis(synopsis);
        Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
        assertEquals(synopsis, article.getSynopsis());
    }

    @Test
    void shouldConvertArticleDTOToArticleAndReturnNotNullContent() {
        ArticleDTO articleDTO = new ArticleDTO();
        Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
        assertNotNull(article.getContent());
    }

    @Test
    void shouldConvertArticleDTOToArticleAndReturnRightContent() {
        ArticleDTO articleDTO = new ArticleDTO();
        String content = "Content";
        articleDTO.setContent(content);
        Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
        assertEquals(content, article.getContent().getContent());
    }

    @Test
    void shouldConvertArticleToArticleDTOAndReturnRightId() {
        Article article = getTestArticle();
        Long id = 1L;
        article.setId(id);
        ArticleDTO articleDTO = articleConverter.convertArticleToArticleDTO(article);
        assertEquals(id, articleDTO.getId());
    }

    @Test
    void shouldConvertArticleToArticleDTOAndReturnRightUuid() {
        Article article = getTestArticle();
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        article.setUuid(uuid);
        ArticleDTO articleDTO = articleConverter.convertArticleToArticleDTO(article);
        assertEquals(uuid, articleDTO.getUuid());
    }

    @Test
    void shouldConvertArticleToArticleDTOAndReturnRightTitle() {
        Article article = getTestArticle();
        String title = "Title";
        article.setTitle(title);
        ArticleDTO articleDTO = articleConverter.convertArticleToArticleDTO(article);
        assertEquals(title, articleDTO.getTitle());
    }

    @Test
    void shouldConvertArticleToArticleDTOAndReturnRightSynopsis() {
        Article article = getTestArticle();
        String synopsis = "Synopsis";
        article.setSynopsis(synopsis);
        ArticleDTO articleDTO = articleConverter.convertArticleToArticleDTO(article);
        assertEquals(synopsis, articleDTO.getSynopsis());
    }

    @Test
    void shouldConvertArticleToArticleDTOAndReturnRightFirstAndLastNameString() {
        Article article = getTestArticle();
        User user = article.getUser();
        String firstAndLastNameString = getFirstAndLastNameString(user);
        ArticleDTO articleDTO = articleConverter.convertArticleToArticleDTO(article);
        assertEquals(firstAndLastNameString, articleDTO.getFirstAndLastName());
    }

    @Test
    void shouldConvertArticleToArticleDTOAndReturnRightIsAuthorDeleted() {
        Article article = getTestArticle();
        ArticleDTO articleDTO = articleConverter.convertArticleToArticleDTO(article);
        assertFalse(articleDTO.getIsAuthorDeleted());
    }

    @Test
    void shouldConvertArticleToArticleDTOAndReturnRightAdditionDateTime() {
        Article article = getTestArticle();
        String formattedDateTime = formatDateTime(article.getAdditionDateTime());
        ArticleDTO articleDTO = articleConverter.convertArticleToArticleDTO(article);
        assertEquals(formattedDateTime, articleDTO.getAdditionDateTime());
    }

    @Test
    void shouldConvertArticleToDetailedArticleDTOAndReturnNotNullObject() {
        Article article = getTestArticle();
        ArticleDTO articleDTO = articleConverter.convertArticleToDetailedArticleDTO(article);
        assertNotNull(articleDTO);
    }

    @Test
    void shouldConvertArticleToDetailedArticleDTOAndReturnRightContent() {
        Article article = getTestArticle();
        ArticleDTO articleDTO = articleConverter.convertArticleToDetailedArticleDTO(article);
        assertEquals(article.getContent().getContent(), articleDTO.getContent());
    }

    @Test
    void shouldConvertArticleToDetailedArticleDTOAndReturnEmptyCommentCollection() {
        Article article = getTestArticle();
        ArticleDTO articleDTO = articleConverter.convertArticleToDetailedArticleDTO(article);
        assertTrue(articleDTO.getComments().isEmpty());
    }

    @Test
    void shouldConvertArticleToDetailedArticleDTOAndReturnNotEmptyCommentCollection() {
        Article article = getTestArticle();
        Set<Comment> comments = new HashSet<>();
        Comment comment = new Comment();
        comment.setAdditionDateTime(LocalDateTime.now());
        comment.setUser(new User());
        comments.add(comment);
        article.setComments(comments);
        ArticleDTO articleDTO = articleConverter.convertArticleToDetailedArticleDTO(article);
        assertEquals(comments.size(), articleDTO.getComments().size());
    }

    @Test
    void shouldConvertEmptyArticleListToArticleDTOList() {
        List<ArticleDTO> articleDTOs = articleConverter.convertArticleListToArticleDTOList(Collections.emptyList());
        assertTrue(articleDTOs.isEmpty());
    }

    @Test
    void shouldConvertNotEmptyArticleListToArticleDTOList() {
        List<Article> articles = new ArrayList<>();
        articles.add(getTestArticle());
        List<ArticleDTO> articleDTOs = articleConverter.convertArticleListToArticleDTOList(articles);
        assertEquals(articles.size(), articleDTOs.size());
    }

    private Article getTestArticle() {
        Article article = new Article();
        article.setAdditionDateTime(LocalDateTime.of(1987, 11, 14, 2,0));
        User user = new User();
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setIsDeleted(false);
        article.setUser(user);
        ArticleContent articleContent = new ArticleContent();
        articleContent.setContent("Content");
        article.setContent(articleContent);
        return article;
    }
}