package com.gmail.yauheniylebedzeu.service.model;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class UserDTO {

    private Long id;
    private String uuid;
    private String lastName;
    private String firstName;
    private String patronymic;
    private String email;
    private String password;
    private RoleDTOEnum role;
    private Set<ReviewDTO> reviews = new HashSet<>();

}
