package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class ArticleDTO {

    private Long id;
    private String uuid;
    @NotNull
    @Size(min = 3, max = 30)
    private String title;
    @NotNull
    @Size(min = 20, max = 200)
    private String synopsis;
    @NotNull
    @Size(min = 100, max = 1000)
    private String content;
    private LocalDate additionDate;
    private String userFirstName;
    private String userLastName;
    private Set<CommentDTO> comments = new HashSet<>();
}
