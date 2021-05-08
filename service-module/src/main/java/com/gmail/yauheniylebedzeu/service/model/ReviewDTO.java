package com.gmail.yauheniylebedzeu.service.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
public class ReviewDTO {

    private Long id;
    private String uuid;
    private String content;
    private LocalDate additionDate;
    private Boolean isVisible;
    private String userLastName;
    private String userFirstName;
    private String userPatronymic;

}
