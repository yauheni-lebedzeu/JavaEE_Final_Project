package com.gmail.yauheniylebedzeu.service.model;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ARTICLE_CONTENT;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ARTICLE_SYNOPSIS;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_LENGTH_OF_ARTICLE_TITLE;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_CONTENT;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_SYNOPSIS;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_LENGTH_OF_ARTICLE_TITLE;

@Data
public class ArticleDTO {

    private Long id;
    private String uuid;

    @NotBlank(message = "This field cannot be empty or consist of only spaces!")
    @Size(min = MIN_LENGTH_OF_ARTICLE_TITLE, max = MAX_LENGTH_OF_ARTICLE_TITLE,
            message = "The article title must be between {min} and {max} characters long!")
    private String title;

    @NotBlank(message = "This field cannot be empty or consist of only spaces!")
    @Size(min = MIN_LENGTH_OF_ARTICLE_SYNOPSIS, max = MAX_LENGTH_OF_ARTICLE_SYNOPSIS,
            message = "The article synopsis must be between {min} and {max} characters long!")
    private String synopsis;

    @NotBlank(message = "This field cannot be empty or consist of only spaces!")
    @Size(min = MIN_LENGTH_OF_ARTICLE_CONTENT, max = MAX_LENGTH_OF_ARTICLE_CONTENT,
            message = "The article text must be between {min} and {max} characters long!")
    private String content;
    private String additionDateTime;
    private String firstAndLastName;
    private Boolean isAuthorDeleted;

    @ToString.Exclude
    private List<CommentDTO> comments = new ArrayList<>();

    public void addComments(List<CommentDTO> comments) {
        this.comments.addAll(comments);
    }
}