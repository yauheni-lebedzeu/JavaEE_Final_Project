package com.gmail.yauheniylebedzeu.service.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class CommentDTO {

    private Long id;
    private String uuid;
    private String content;
    private LocalDate additionDate;
    private String userFirstName;
    private String userLastName;
}
