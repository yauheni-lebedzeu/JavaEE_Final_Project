package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.Article;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;

import java.util.List;

public interface ArticleConverter {

    Article convertArticleDTOToArticle(ArticleDTO articleDTO);

    ArticleDTO convertArticleToArticleDTO(Article article);

    ArticleDTO convertArticleToDetailedArticleDTO(Article newsItem);

    List<ArticleDTO> convertArticleListToArticleDTOList(List<Article> articles);

}
