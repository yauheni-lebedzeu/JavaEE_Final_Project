package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.*;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getUserPrincipal;

@Controller
@AllArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final UserService userService;

    @GetMapping(value = ARTICLES_CONTROLLER_URL)
    public String getArticles(@RequestParam(defaultValue = "1") int pageNumber,
                              @RequestParam(defaultValue = "10") int pageSize, Model model) {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        if (optionalUser.isPresent()) {
            UserDTO loggedInUser = optionalUser.get();
            RoleDTOEnum role = loggedInUser.getRole();
            String roleName = role.name();
            model.addAttribute("role", roleName);
            PageDTO<ArticleDTO> page = articleService.getArticlesPage(pageNumber, pageSize, "additionDate desc");
            model.addAttribute("page", page);
            return "articles";
        } else {
            return "redirect:/login";
        }
    }

    @GetMapping(value = ARTICLES_CONTROLLER_URL + "/{uuid}")
    public String getArticle(@PathVariable String uuid, Model model) {
        ArticleDTO article = articleService.findByUuid(uuid);
        model.addAttribute("article", article);
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
            Optional<UserDTO> optionalUser = getUserPrincipal();
            if (optionalUser.isPresent()) {
                UserDTO loggedInUser = optionalUser.get();
                String uuid = loggedInUser.getUuid();
                articleService.add(uuid, article);
            } else {
                return "redirect:/login";
            }
            return "redirect:" + ARTICLES_CONTROLLER_URL;
        }
    }
}