package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ArticleRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.ArticleContent;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.converter.ArticleConverter;
import com.gmail.yauheniylebedzeu.service.exception.ArticleNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
@Log4j2
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final ArticleConverter articleConverter;

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticlesPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<ArticleDTO> page = new PageDTO<>();
        Long countOfArticles = articleRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfArticles, pageSize);
        page.setCountOfPages(countOfPages);
        if (pageNumber > countOfPages) {
            pageNumber = countOfPages;
        }
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Article> news = articleRepository.findEntitiesWithLimit(startPosition, pageSize, sortParameter);
        List<ArticleDTO> newsDTO = news.stream()
                .map(articleConverter::convertArticleToArticleDTO)
                .collect(Collectors.toList());
        List<ArticleDTO> articlesOnPage = page.getObjects();
        articlesOnPage.addAll(newsDTO);
        return page;
    }

    @Override
    @Transactional
    public ArticleDTO findArticleByUuid(String uuid) {
        try {
            Article article = articleRepository.findByUuid(uuid);
            return articleConverter.convertArticleToDetailedArticleDTO(article);
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            throw new ArticleNotFoundException(String.format("The article with uuid %s was not found in the database", uuid));
        }
    }

    @Override
    @Transactional
    public List<ArticleDTO> findAll() {
        List<Article> articles = articleRepository.findAll();
        return articleConverter.convertArticleListToArticleDTOList(articles);
    }

    @Override
    @Transactional
    public ArticleDTO addArticle(String userUuid, ArticleDTO articleDTO) {
        try {
            User user = userRepository.findByUuid(userUuid);
            Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
            article.setUser(user);
            articleRepository.persist(article);
            return articleConverter.convertArticleToArticleDTO(article);
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            throw new UserNotFoundException(String.format("The user with uuid %s was not found in the database", userUuid));
        }
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        try {
            Article article = articleRepository.findByUuid(uuid);
            articleRepository.remove(article);
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            throw new ArticleNotFoundException(String.format("The article with uuid %s was not found in the database", uuid));
        }
    }
}
