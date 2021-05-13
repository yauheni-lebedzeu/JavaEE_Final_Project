package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;

import java.util.List;

public interface ArticleService {

    PageDTO<ArticleDTO> getArticlesPage(int pageNumber, int pageSize, String sortParameter);

    ArticleDTO findByUuid(String uuid);

    List<ArticleDTO> findAll();

    ArticleDTO add(String userUuid, ArticleDTO articleDTO);

    void removeByUuid(String uuid);
}
