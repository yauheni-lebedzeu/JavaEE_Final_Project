package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ArticlesAPIController {

    private final ArticleService articleService;

    @Lazy
    public ArticlesAPIController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping(value = "/articles")
    public ResponseEntity<?> getArticles() {
        List<ArticleDTO> articles = articleService.findAll();
        if (!articles.isEmpty()) {
            return new ResponseEntity<>(articles, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(value = "/articles/{uuid}")
    public ResponseEntity<?> getArticle(@PathVariable String uuid) {
        ArticleDTO article = articleService.findArticleByUuid(uuid);
        return new ResponseEntity<>(article, HttpStatus.OK);
    }

    @PostMapping(value = "/articles/{userUuid}")
    public ResponseEntity<?> addArticle(@PathVariable String userUuid, @RequestBody ArticleDTO articleDTO) {
            articleService.addArticle(userUuid, articleDTO);
            return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/articles/{uuid}")
    public ResponseEntity<?> deleteArticle(@PathVariable String uuid) {
        articleService.removeByUuid(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
