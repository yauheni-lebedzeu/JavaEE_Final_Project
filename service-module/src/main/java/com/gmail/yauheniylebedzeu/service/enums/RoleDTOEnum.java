package com.gmail.yauheniylebedzeu.service.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RoleDTOEnum {

    ADMIN("Administrator"),
    SALE_USER("Seller"),
    CUSTOMER_USER("Customer"),
    SECURE_REST_API("Secure REST API");

    private final String name;

}
