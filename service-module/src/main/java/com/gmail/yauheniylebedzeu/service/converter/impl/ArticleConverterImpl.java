package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.repository.model.ArticleContent;
import com.gmail.yauheniylebedzeu.repository.model.Comment;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.ArticleConverter;
import com.gmail.yauheniylebedzeu.service.converter.CommentConverter;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getArticleContent;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getComments;
import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUser;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.formatDateTime;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getFirstAndLastNameString;

@Component
@AllArgsConstructor
public class ArticleConverterImpl implements ArticleConverter {

    private final CommentConverter commentConverter;

    @Override
    public Article convertArticleDTOToArticle(ArticleDTO articleDTO) {
        Article article = new Article();
        article.setTitle(articleDTO.getTitle());
        article.setSynopsis(articleDTO.getSynopsis());
        article.setAdditionDateTime(LocalDateTime.now().withNano(0));
        ArticleContent articleContent = new ArticleContent();
        articleContent.setContent(articleDTO.getContent());
        articleContent.setArticle(article);
        article.setContent(articleContent);
        return article;
    }

    @Override
    public ArticleDTO convertArticleToArticleDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setUuid(article.getUuid());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setSynopsis(article.getSynopsis());
        LocalDateTime additionDateTime = article.getAdditionDateTime();
        String formattedDateTime = formatDateTime(additionDateTime);
        articleDTO.setAdditionDateTime(formattedDateTime);
        User user = getUser(article);
        String firstAndLastName = getFirstAndLastNameString(user);
        articleDTO.setFirstAndLastName(firstAndLastName);
        articleDTO.setIsAuthorDeleted(user.getIsDeleted());
        return articleDTO;
    }

    @Override
    public ArticleDTO convertArticleToDetailedArticleDTO(Article article) {
        ArticleDTO articleDTO = convertArticleToArticleDTO(article);
        ArticleContent articleContent = getArticleContent(article);
        articleDTO.setContent(articleContent.getContent());
        Set<Comment> comments = getComments(article);
        if (!comments.isEmpty()) {
            List<CommentDTO> commentDTOs = comments.stream()
                    .map(commentConverter::convertCommentToCommentDTO)
                    .collect(Collectors.toList());
            articleDTO.addComments(commentDTOs);
        }
        return articleDTO;
    }

    @Override
    public List<ArticleDTO> convertArticleListToArticleDTOList(List<Article> articles) {
        return articles.stream()
                .map(this::convertArticleToArticleDTO)
                .collect(Collectors.toList());
    }
}