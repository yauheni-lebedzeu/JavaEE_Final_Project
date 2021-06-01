package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ArticleRepository;
import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.ArticleContent;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.ArticleConverter;
import com.gmail.yauheniylebedzeu.service.exception.ArticleNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getArticleContent;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.checkPageNumber;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleConverter articleConverter;
    private final UserService userService;

    @Override
    @Transactional
    public PageDTO<ArticleDTO> getArticlesPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<ArticleDTO> page = new PageDTO<>();
        Long countOfArticles = articleRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfArticles, pageSize);
        page.setCountOfPages(countOfPages);
        pageNumber = checkPageNumber(pageNumber, countOfPages);
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Article> news = articleRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<ArticleDTO> newsDTO = articleConverter.convertArticleListToArticleDTOList(news);
        page.addObjects(newsDTO);
        return page;
    }

    @Override
    @Transactional
    public ArticleDTO findByUuid(String uuid) {
        Article article = getSafeArticle(uuid);
        return articleConverter.convertArticleToDetailedArticleDTO(article);
    }

    @Override
    @Transactional
    public List<ArticleDTO> findAll() {
        List<Article> articles = articleRepository.findAll();
        return articleConverter.convertArticleListToArticleDTOList(articles);
    }

    @Override
    @Transactional
    public ArticleDTO add(String userUuid, ArticleDTO articleDTO) {
        User user = userService.getSafeUser(userUuid);
        Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
        article.setUser(user);
        articleRepository.persist(article);
        return articleConverter.convertArticleToArticleDTO(article);
    }

    @Override
    @Transactional
    public ArticleDTO edit(ArticleDTO articleDTO) {
        String uuid = articleDTO.getUuid();
        Article article = getSafeArticle(uuid);
        ArticleContent articleContent = getArticleContent(article);
        articleContent.setContent(articleDTO.getContent());
        article.setTitle(articleDTO.getTitle());
        article.setSynopsis(articleDTO.getSynopsis());
        article.setAdditionDateTime(LocalDateTime.now());
        articleRepository.merge(article);
        return articleConverter.convertArticleToDetailedArticleDTO(article);
    }

    @Override
    @Transactional
    public boolean removeByUuid(String uuid) {
        Article article = getSafeArticle(uuid);
        articleRepository.remove(article);
        return true;
    }

    @Override
    @Transactional
    public Article getSafeArticle(String articleUuid) {
        Optional<Article> optionalArticle = articleRepository.findByUuid(articleUuid);
        if (optionalArticle.isPresent()) {
            return optionalArticle.get();
        } else {
            throw new ArticleNotFoundException(String.format("The article with uuid %s was not found", articleUuid));
        }
    }
}