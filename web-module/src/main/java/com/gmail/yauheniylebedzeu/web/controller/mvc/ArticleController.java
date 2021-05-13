package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserLogin;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import static com.gmail.yauheniylebedzeu.web.controller.constant.AttributeNameConstant.*;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.*;

@Controller
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping(value = ARTICLES_CONTROLLER_URL)
    public String getArticles(@RequestParam(defaultValue = "1") int pageNumber,
                              @RequestParam(defaultValue = "10") int pageSize, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserLogin userLogin = (UserLogin) authentication.getPrincipal();
            UserDTO user = userLogin.getUser();
            RoleDTOEnum role = user.getRole();
            String roleName = role.name();
            model.addAttribute(ROLE_ATTRIBUTE_NAME, roleName);
            PageDTO<ArticleDTO> page = articleService.getArticlesPage(pageNumber, pageSize, "additionDate desc");
            model.addAttribute(PAGE_ATTRIBUTE_NAME, page);
            return "articles";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(value = ARTICLES_CONTROLLER_URL + "/{uuid}")
    public String getArticle(@PathVariable String uuid, Model model) {
        ArticleDTO article = articleService.findByUuid(uuid);
        model.addAttribute(ARTICLE_ATTRIBUTE_NAME, article);
        return "article";
    }

    @PostMapping(value = SELLER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL + DEL_CONTROLLER_URL + "/{uuid}/{sourcePageNumber}")
    public String delArticle(@PathVariable String sourcePageNumber,
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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                UserLogin userLogin = (UserLogin) authentication.getPrincipal();
                UserDTO loggedInUser = userLogin.getUser();
                String uuid = loggedInUser.getUuid();
                articleService.add(uuid, article);
            }
            return "redirect:" + ARTICLES_CONTROLLER_URL;
        }
    }
}
