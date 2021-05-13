package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.ArticleRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.converter.ArticleConverter;
import com.gmail.yauheniylebedzeu.service.exception.ArticleNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
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
        if (pageNumber > countOfPages && countOfPages != 0) {
            pageNumber = countOfPages;
        }
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<Article> news = articleRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<ArticleDTO> newsDTO = articleConverter.convertArticleListToArticleDTOList(news);
        List<ArticleDTO> articlesOnPage = page.getObjects();
        articlesOnPage.addAll(newsDTO);
        return page;
    }

    @Override
    @Transactional
    public ArticleDTO findByUuid(String uuid) {
        Optional<Article> optionalArticle = articleRepository.findByUuid(uuid);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            return articleConverter.convertArticleToDetailedArticleDTO(article);
        } else {
            throw new ArticleNotFoundException(String.format("The article with uuid %s was not found in the database",
                    uuid));
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
    public ArticleDTO add(String userUuid, ArticleDTO articleDTO) {
        Optional<User> optionalUser = userRepository.findByUuid(userUuid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Article article = articleConverter.convertArticleDTOToArticle(articleDTO);
            article.setUser(user);
            articleRepository.persist(article);
            return articleConverter.convertArticleToArticleDTO(article);
        } else {
            throw new UserNotFoundException(String.format("The user with uuid %s was not found in the database",
                    userUuid));
        }
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        Optional<Article> optionalArticle = articleRepository.findByUuid(uuid);
        if (optionalArticle.isPresent()) {
            Article article = optionalArticle.get();
            articleRepository.remove(article);
        } else {
            throw new ArticleNotFoundException(String.format("The article with uuid %s was not found in the database",
                    uuid));
        }
    }
}
