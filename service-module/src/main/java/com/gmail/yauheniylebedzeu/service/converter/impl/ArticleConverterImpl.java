package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.ArticleContent;
import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.ArticleConverter;
import com.gmail.yauheniylebedzeu.service.converter.CommentConverter;
import com.gmail.yauheniylebedzeu.service.exception.UserNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ArticleConverterImpl implements ArticleConverter {

    private final CommentConverter commentConverter;

    @Override
    public Article convertArticleDTOToArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setSynopsis(articleDTO.getSynopsis());
        ArticleContent articleContent = new ArticleContent();
        articleContent.setContent(articleDTO.getContent());
        article.setContent(articleContent);
        articleContent.setArticle(article);
        return article;
    }

    @Override
    public ArticleDTO convertArticleToArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setUuid(article.getUuid());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setSynopsis(article.getSynopsis());
        articleDTO.setAdditionDate(article.getAdditionDate());
        User user = article.getUser();
        if (Objects.isNull(user)) {
            throw new UserNotReceivedException(String.format("Couldn't get the user from the database for the article" +
                    " with id = %d", article.getId()));
        }
        articleDTO.setUserFirstName(user.getFirstName());
        articleDTO.setUserLastName(user.getLastName());
        return articleDTO;
    }

    @Override
    public ArticleDTO convertArticleToDetailedArticleDTO(Article article) {
        ArticleDTO articleDTO = convertArticleToArticleDTO(article);
        Set<Comment> comments = article.getComments();
        if (!comments.isEmpty()) {
            Set<CommentDTO> commentDTOs = comments.stream()
                    .map(commentConverter::convertCommentToCommentDTO)
                    .collect(Collectors.toSet());
            Set<CommentDTO> emptyCommentsDTOs = articleDTO.getComments();
            emptyCommentsDTOs.addAll(commentDTOs);
            ArticleContent articleContent = article.getContent();
            articleDTO.setContent(articleContent.getContent());
        }
        return articleDTO;
    }

    @Override
    public List<ArticleDTO> convertArticleListToArticleDTOList(List<Article> articles) {
        return articles.stream()
                .map(this::convertArticleToDetailedArticleDTO)
                .collect(Collectors.toList());
    }
}
