package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.service.ArticleService;
import com.gmail.yauheniylebedzeu.service.converter.BindingResultConverter;
import com.gmail.yauheniylebedzeu.service.model.ArticleDTO;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;

@RestController
@RequestMapping(value = API_CONTROLLER_URL)
@AllArgsConstructor
public class ArticleAPIController {

    private final ArticleService articleService;
    private final BindingResultConverter bindingResultConverter;


    @GetMapping(value = ARTICLES_CONTROLLER_URL)
    public List<ArticleDTO> getArticles() {
        return articleService.findAll();
    }

    @GetMapping(value = ARTICLES_CONTROLLER_URL + "/{uuid}")
    public ArticleDTO getArticle(@PathVariable String uuid) {
        return articleService.findByUuid(uuid);
    }

    @PostMapping(value = ARTICLES_CONTROLLER_URL + "/{userUuid}")
    public ResponseEntity<?> addArticle(@PathVariable String userUuid, @RequestBody @Valid ArticleDTO articleDTO,
                                           BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            ErrorsDTO errors = bindingResultConverter.convertBindingResultToErrorsDTO(bindingResult);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        articleService.add(userUuid, articleDTO);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping(value = ARTICLES_CONTROLLER_URL + "/{uuid}")
    public ResponseEntity<Void> deleteArticle(@PathVariable String uuid) {
        articleService.removeByUuid(uuid);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}