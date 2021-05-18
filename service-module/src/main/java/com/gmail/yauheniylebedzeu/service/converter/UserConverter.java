package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;

import java.util.List;

public interface UserConverter {

    User convertUserDTOToUser(UserDTO userDTO);

    UserDTO convertUserToUserDTO(User user);

    UserDTO convertUserToUserDTOWithContacts(User user);

    List<UserDTO> convertUserListToUserDTOList(List<User> users);
}
