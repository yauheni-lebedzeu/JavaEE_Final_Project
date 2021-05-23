package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.gmail.yauheniylebedzeu.service.constant.MessagesPropertyConstant.NOT_EMPTY_FIELD_PROPERTY;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String uuid;

    @NotBlank(message = "{" + NOT_EMPTY_FIELD_PROPERTY + "}")
    @Size(min = 3, max = 200)
    private String content;
    private String additionDateTime;
    private String firstAndLastName;
}
