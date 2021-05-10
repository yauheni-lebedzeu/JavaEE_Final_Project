package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@AllArgsConstructor
public class ArticlesAPIController {

    private final ArticleService articleService;


    @GetMapping(value = "/articles")
    public List<ArticleDTO> getArticles() {
        List<ArticleDTO> articles = articleService.findAll();
        return articles;
    }

    @GetMapping(value = "/articles/{uuid}")
    public ArticleDTO getArticle(@PathVariable String uuid) {
        return articleService.findArticleByUuid(uuid);
    }

    @PostMapping(value = "/articles/{userUuid}")
    public ResponseEntity<Void> addArticle(@PathVariable String userUuid, @RequestBody ArticleDTO articleDTO) {
        articleService.addArticle(userUuid, articleDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/articles/{uuid}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String uuid) {
        articleService.removeByUuid(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
