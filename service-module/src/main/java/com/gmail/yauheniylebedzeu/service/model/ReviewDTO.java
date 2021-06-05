package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MAX_REVIEW_CONTENT_LENGTH;
import static com.gmail.yauheniylebedzeu.service.constant.ValidationConstant.MIN_REVIEW_CONTENT_LENGTH;

@Getter
@Setter
public class ReviewDTO {

    private Long id;
    private String uuid;

    @NotBlank(message = "This field cannot be empty or consist of only spaces!")
    @Size(min = MIN_REVIEW_CONTENT_LENGTH, max = MAX_REVIEW_CONTENT_LENGTH,
            message = "The review text must be between {min} and {max} characters long!")
    private String content;
    private String additionDateTime;
    private Boolean isVisible;
    private String fullName;
    private Boolean isAuthorDeleted;
}
