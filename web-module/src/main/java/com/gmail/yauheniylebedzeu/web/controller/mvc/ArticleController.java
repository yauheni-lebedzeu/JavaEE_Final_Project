package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.CommentService;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.CommentDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADD_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.COMMENTS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.DELETE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.SELLER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserRoleName;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserUuid;

@Controller
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final CommentService commentService;

    @GetMapping(value = ARTICLES_CONTROLLER_URL)
    public String getArticles(@RequestParam(defaultValue = "1") int pageNumber,
                              @RequestParam(defaultValue = "10") int pageSize, Model model) {
        String roleName = getLoggedUserRoleName();
        model.addAttribute("role", roleName);
        PageDTO<ArticleDTO> page = articleService.getArticlesPage(pageNumber, pageSize, "additionDateTime desc");
        model.addAttribute("page", page);
        return "articles";
    }

    @GetMapping(value = ARTICLES_CONTROLLER_URL + "/{articleUuid}")
    public String getArticle(@PathVariable String articleUuid, CommentDTO commentDTO,
                             BindingResult errors, Model model) {
        String roleName = getLoggedUserRoleName();
        model.addAttribute("role", roleName);
        ArticleDTO article = articleService.findByUuid(articleUuid);
        model.addAttribute("article", article);
        return "article";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + DELETE_CONTROLLER_URL + "/{uuid}/{sourcePageNumber}")
    public String deleteArticle(@PathVariable String sourcePageNumber,
                                @PathVariable String uuid) {
        articleService.removeByUuid(uuid);
        return "redirect:" + ARTICLES_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @GetMapping(value = SELLER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + ADD_CONTROLLER_URL)
    public String getArticleForm(ArticleDTO articleDTO) {
        return "article-form";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + ADD_CONTROLLER_URL)
    public String addArticle(@Valid ArticleDTO article, BindingResult errors) {
        if (errors.hasErrors()) {
            return "article-form";
        } else {
            String uuid = getLoggedUserUuid();
            articleService.add(uuid, article);
            return "redirect:" + ARTICLES_CONTROLLER_URL;
        }
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/{articleUuid}"
            + ADD_CONTROLLER_URL + COMMENTS_CONTROLLER_URL)
    public String addComment(@PathVariable String articleUuid, @Valid CommentDTO commentDTO,
                             BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            return getArticle(articleUuid, commentDTO, errors, model);
        } else {
            String userUuid = getLoggedUserUuid();
            commentService.addCommentToArticle(userUuid, articleUuid, commentDTO);
            return "redirect:" + ARTICLES_CONTROLLER_URL + "/" + articleUuid;
        }
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/{articleUuid}"
            + COMMENTS_CONTROLLER_URL + DELETE_CONTROLLER_URL)
    public String deleteComment(@PathVariable String articleUuid,
                                @RequestParam List<String> commentUuids) {
        commentUuids.forEach(commentService::deleteComment);
        return "redirect:" + ARTICLES_CONTROLLER_URL + "/" + articleUuid;
    }

    @GetMapping(value = SELLER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + "/{articleUuid}" + ControllerUrlConstant.EDIT_CONTROLLER_URL)
    public String getArticleEditor(@PathVariable String articleUuid, ArticleDTO articleDTO, Model model) {
        articleDTO = articleService.findByUuid(articleUuid);
        model.addAttribute("articleDTO", articleDTO);
        return "article-editor";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + ControllerUrlConstant.EDIT_CONTROLLER_URL)
    public String editArticle(@Valid ArticleDTO articleDTO, BindingResult errors, Model model) {
        if (errors.hasErrors()) {
            model.addAttribute("originalArticle", articleDTO);
            return "article-editor";
        } else {
            ArticleDTO editedArticle = articleService.edit(articleDTO);
            return "redirect:" + ARTICLES_CONTROLLER_URL + "/" + editedArticle.getUuid();
        }
    }
}