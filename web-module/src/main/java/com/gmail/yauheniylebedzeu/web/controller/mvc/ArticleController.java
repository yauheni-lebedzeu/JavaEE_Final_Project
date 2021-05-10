package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;

@Controller
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping(value = CUSTOMER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL)
    public String getArticles(@RequestParam(defaultValue = "1") int pageNumber,
                              @RequestParam(defaultValue = "10") int pageSize, Model model) {
        PageDTO<ArticleDTO> page = articleService.getArticlesPage(pageNumber, pageSize, "additionDate desc");
        model.addAttribute("page", page);
        return "articles";
    }

    @GetMapping(value = CUSTOMER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/{uuid}")
    public String getArticle(@PathVariable String uuid, Model model) {
        ArticleDTO article = articleService.findArticleByUuid(uuid);
        model.addAttribute("article", article);
        return "article";
    }

}
