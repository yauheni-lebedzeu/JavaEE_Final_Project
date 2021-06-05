package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ArticleRepository;
import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.ArticleContent;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.ArticleConverter;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ArticleServiceImplTest {

    @Mock
    private ArticleRepository articleRepository;

    @Mock
    private ArticleConverter articleConverter;

    @Mock
    private UserService userService;

    @InjectMocks
    private ArticleServiceImpl articleService;

    @Test
    void shouldFindArticleByUuidAndGetArticleDTO() {
        String uuid = UUID.randomUUID().toString();
        Article article = new Article();
        article.setUuid(uuid);
        when(articleRepository.findByUuid(uuid)).thenReturn(Optional.of(article));
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setUuid(uuid);
        when(articleConverter.convertArticleToDetailedArticleDTO(article)).thenReturn(articleDTO);
        ArticleDTO resultArticleDTO = articleService.findByUuid(uuid);
        assertEquals(uuid,
                resultArticleDTO.getUuid());
    }

    @Test
    void shouldFindAllAndGetEmptyArticleDTOList() {
        List<Article> articles = Collections.emptyList();
        when(articleRepository.findAll()).thenReturn(articles);
        List<ArticleDTO> articleDTOs = Collections.emptyList();
        when(articleConverter.convertArticleListToArticleDTOList(articles)).thenReturn(articleDTOs);
        List<ArticleDTO> resultArticleDTOs = articleService.findAll();
        assertTrue(resultArticleDTOs.isEmpty());
    }

    @Test
    void shouldFindAllAndGetArticleDTOList() {
        Article article = new Article();
        List<Article> articles = Collections.singletonList(article);
        when(articleRepository.findAll()).thenReturn(articles);
        ArticleDTO articleDTO = new ArticleDTO();
        List<ArticleDTO> articleDTOs = Collections.singletonList(articleDTO);
        when(articleConverter.convertArticleListToArticleDTOList(articles)).thenReturn(articleDTOs);
        List<ArticleDTO> resultArticlesDTOs = articleService.findAll();
        assertEquals(articles.size(), resultArticlesDTOs.size());
    }

    @Test
    void shouldAddArticleAndGetArticleWithId() {
        String userUuid = UUID.randomUUID().toString();
        User user = new User();
        when(userService.getSafeUser(userUuid)).thenReturn(user);
        ArticleDTO articleDTO = new ArticleDTO();
        Article article = new Article();
        when(articleConverter.convertArticleDTOToArticle(articleDTO)).thenReturn(article);
        long articleId = 1L;
        articleDTO.setId(articleId);
        when(articleConverter.convertArticleToArticleDTO(article)).thenReturn(articleDTO);
        ArticleDTO resultArticleDTO = articleService.add(userUuid, articleDTO);
        assertEquals(articleId, resultArticleDTO.getId());

    }

    @Test
    void shouldEditArticleAndGetEditedArticleDTO() {
        String articleUuid = UUID.randomUUID().toString();
        Article article = new Article();
        article.setContent(new ArticleContent());
        article.setUuid(articleUuid);
        when(articleRepository.findByUuid(articleUuid)).thenReturn(Optional.of(article));
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setUuid(articleUuid);
        article.setUuid(articleUuid);
        String title = "title";
        articleDTO.setTitle(title);
        String synopsis = "synopsis";
        articleDTO.setSynopsis(synopsis);
        String content = "content";
        articleDTO.setContent(content);
        when(articleConverter.convertArticleToDetailedArticleDTO(article)).thenReturn(articleDTO);
        ArticleDTO resultArticleDTO = articleService.edit(articleDTO);
        assertEquals(title, resultArticleDTO.getTitle());
        assertEquals(synopsis, resultArticleDTO.getSynopsis());
        assertEquals(content, resultArticleDTO.getContent());
    }
}