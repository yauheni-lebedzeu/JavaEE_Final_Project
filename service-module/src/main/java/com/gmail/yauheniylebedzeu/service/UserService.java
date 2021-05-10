package com.gmail.yauheniylebedzeu.service;

import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;

public interface UserService {

    UserDTO add(UserDTO userDTO);

    UserDTO findByEmail(String email);

    Long getCountOfUsers();

    UserDTO changeRoleByUuid(String uuid, String roleName);

    void removeByUuid(String uuid);

    PageDTO<UserDTO> getUserPage(int pageNumber, int pageSize, String sortParameter);

    UserDTO changeParameters(String uuid, UserUpdateDTO userUpdateDTO);

    UserDTO changePasswordByUuid(String uuid);

    void sendPasswordToUser(UserDTO userDTO);
}
