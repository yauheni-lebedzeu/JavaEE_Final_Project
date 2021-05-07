package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;

public interface UserConverter {

    User convertUserDTOtoUser(UserDTO userDTO);

    UserDTO convertUserToUserDTO(User user);

}
