package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageDTO<T> {

    private int countOfPages;
    private int pageNumber;
    private final List<T> objects = new ArrayList<>();

}
