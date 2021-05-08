package com.gmail.yauheniylebedzeu.service.model;

import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
public class ArticleDTO {

    private Long id;
    private String uuid;
    private String title;
    private String synopsis;
    private String content;
    private LocalDate additionDate;
    private String userFirstName;
    private String userLastName;
    private Set<CommentDTO> comments = new HashSet<>();
}
