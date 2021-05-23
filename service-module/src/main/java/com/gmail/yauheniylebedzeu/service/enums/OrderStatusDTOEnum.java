package com.gmail.yauheniylebedzeu.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum  OrderStatusDTOEnum {

    NEW("New"),
    IN_PROGRESS("In progress"),
    DELIVERED ("Delivered"),
    REJECTED("Rejected");

    private final String description;
}