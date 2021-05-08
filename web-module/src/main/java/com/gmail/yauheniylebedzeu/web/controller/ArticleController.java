package com.gmail.yauheniylebedzeu.web.controller;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Lazy
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(value = "/articles")
    public String getArticles(@RequestParam(defaultValue = "1") int pageNumber, Model model) {
        int pageSize = 10;
        PageDTO<ArticleDTO> page = articleService.getArticlesPage(pageNumber, pageSize, "additionDate desc");
        model.addAttribute("page", page);
        return "articles";
    }

    @GetMapping(value = "/articles/{uuid}")
    public String getArticle(@PathVariable String uuid, Model model) {
        ArticleDTO article = articleService.findArticleByUuid(uuid);
        model.addAttribute("article", article);
        return "article";
    }

}
