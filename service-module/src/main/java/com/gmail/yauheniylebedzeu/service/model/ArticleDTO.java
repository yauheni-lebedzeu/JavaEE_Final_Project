package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.constant.MessagesPropertyConstant.NOT_EMPTY_FIELD_PROPERTY;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ARTICLE_CONTENT;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ARTICLE_SYNOPSIS;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ARTICLE_TITLE;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_CONTENT;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_SYNOPSIS;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_TITLE;

@Getter
@Setter
public class ArticleDTO {

    private Long id;
    private String uuid;

    @NotBlank(message = "{" + NOT_EMPTY_FIELD_PROPERTY + "}")
    @Size(min = MIN_LENGTH_OF_ARTICLE_TITLE, max = MAX_LENGTH_OF_ARTICLE_TITLE)
    private String title;

    @NotBlank(message = "{" + NOT_EMPTY_FIELD_PROPERTY + "}")
    @Size(min = MIN_LENGTH_OF_ARTICLE_SYNOPSIS, max = MAX_LENGTH_OF_ARTICLE_SYNOPSIS)
    private String synopsis;

    @NotBlank(message = "{" + NOT_EMPTY_FIELD_PROPERTY + "}")
    @Size(min = MIN_LENGTH_OF_ARTICLE_CONTENT, max = MAX_LENGTH_OF_ARTICLE_CONTENT)
    private String content;
    private String additionDateTime;
    private String firstAndLastName;
    private Set<CommentDTO> comments = new HashSet<>();
}