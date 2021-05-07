package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.WrongRoleNameException;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

@Component
public class UserConverterImpl implements UserConverter {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public User convertUserDTOtoUser(UserDTO userDTO) {
        User user = new User();
        user.setLastName(userDTO.getLastName());
        user.setFirstName(userDTO.getFirstName());
        user.setPatronymic(userDTO.getPatronymic());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
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
        if (!Objects.isNull(role)) {
            RoleEnum roleEnum = role.getName();
            String roleName = roleEnum.name();
            try {
                RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleName);
                userDTO.setRole(roleDTOEnum);
            } catch (IllegalArgumentException e) {
                logger.error(e.getMessage(), e);
                throw new WrongRoleNameException("\"" + roleName + "\" — an invalid role name");
            }
        }
        return userDTO;
    }
}
