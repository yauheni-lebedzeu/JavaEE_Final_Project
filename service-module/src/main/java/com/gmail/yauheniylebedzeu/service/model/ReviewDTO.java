package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ReviewDTO {

    private Long id;
    private String uuid;
    private String content;
    private LocalDate additionDate;
    private Boolean isVisible;
    private String fullName;
}
