package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Getter
@Setter
public class CommentDTO {

    private Long id;
    private String uuid;
    @NotNull
    @Size(min = 3, max = 200)
    private String content;
    private LocalDate additionDate;
    private String userFirstName;
    private String userLastName;
}
