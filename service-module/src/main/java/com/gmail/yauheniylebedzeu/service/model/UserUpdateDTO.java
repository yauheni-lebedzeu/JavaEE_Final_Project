package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDTO {

    private String firstName;
    private String lastName;
    private String address;
    private String phoneNumber;
    private String password;
    private String reEnteredPassword;
}
