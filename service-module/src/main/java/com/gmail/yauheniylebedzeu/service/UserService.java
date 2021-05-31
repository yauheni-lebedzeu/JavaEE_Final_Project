package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;

import java.util.List;

public interface UserService {

    UserDTO add(UserDTO userDTO);

    UserDTO findByEmail(String email);

    Long getCountOfUsers();

    UserDTO changeRoleByUuid(String uuid, String roleName);

    void removeByUuid(String uuid);

    PageDTO<UserDTO> getUserPage(int pageNumber, int pageSize, String sortParameter);

    UserDTO changeParameters(String uuid, UserUpdateDTO userUpdateDTO);

    UserDTO changePasswordByUuid(String uuid);

    List<UserDTO> findAll();

    User getSafeUser(String userUuid);

    UserDTO restore(String userUuid);
}
