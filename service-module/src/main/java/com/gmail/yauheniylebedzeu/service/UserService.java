package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.UserDTO;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void add(UserDTO userDTO);

    Optional<UserDTO> getByEmail(String email);

    Long getCountOfUsers();

    List<UserDTO> findAll(int startPosition, int maxResult, String sortFieldName);

    Optional<UserDTO> changePasswordByUuid(String uuid);

    Optional<UserDTO> changeRoleByUuid(String uuid, String roleName);

    void removeByUuid(String uuid);

}
