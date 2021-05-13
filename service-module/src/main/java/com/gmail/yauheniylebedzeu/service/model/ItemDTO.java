package com.gmail.yauheniylebedzeu.service.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ItemDTO {

    private Long uniqueNumber;
    private String uuid;
    private String name;
    private BigDecimal price;
    private String description;

}
