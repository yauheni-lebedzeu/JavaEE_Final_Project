package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class UserConverterImpl implements UserConverter {

    @Override
    public User convertUserDTOToUser(UserDTO userDTO) {
        User user = new User();
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setPatronymic(userDTO.getPatronymic());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        String address = userDTO.getAddress();
        String phoneNumber = userDTO.getPhoneNumber();
        if (StringUtils.isNotBlank(address) || StringUtils.isNotBlank(phoneNumber)) {
            UserContacts contacts = new UserContacts();
            contacts.setAddress(address);
            contacts.setPhoneNumber(phoneNumber);
            contacts.setUser(user);
            user.setContacts(contacts);
        }
        return user;
    }

    @Override
    public UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUuid(user.getUuid());
        userDTO.setLastName(user.getLastName());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setPatronymic(user.getPatronymic());
        userDTO.setEmail(user.getEmail());
        userDTO.setPassword(user.getPassword());
        Role role = user.getRole();
        if (Objects.isNull(role)) {
            throw new RoleNotReceivedException(String.format("Couldn't get the role from the database for the user" +
                    " with id = %d", user.getId()));
        }
        RoleEnum roleEnum = role.getName();
        String roleName = roleEnum.name();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleName);
        userDTO.setRole(roleDTOEnum);
        return userDTO;
    }

    @Override
    public UserDTO convertUserToUserDTOWithContacts(User user) {
        UserDTO userDTO = convertUserToUserDTO(user);
        UserContacts contacts = user.getContacts();
        if (!Objects.isNull(contacts)) {
            userDTO.setAddress(contacts.getAddress());
            userDTO.setPhoneNumber(contacts.getPhoneNumber());
        }
        return userDTO;
    }

    @Override
    public List<UserDTO> convertUserListToUserDTOList(List<User> users) {
        return users.stream()
                .map(this::convertUserToUserDTO)
                .collect(Collectors.toList());
    }
}
