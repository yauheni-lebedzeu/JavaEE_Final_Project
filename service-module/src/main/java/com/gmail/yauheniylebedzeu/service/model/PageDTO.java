package com.gmail.yauheniylebedzeu.service.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PageDTO<T> {

    private int countOfPages;
    private int pageNumber;
    private final List<T> objects = new ArrayList<>();

}
