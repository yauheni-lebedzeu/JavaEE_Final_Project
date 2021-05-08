package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotReceivedException;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserConverterImplTest {

    private final UserConverterImpl userConverter = new UserConverterImpl();

    @Test
    void shouldConvertUserDTOToUserAndGetNotNullObject() {
        UserDTO userDTO = new UserDTO();
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertNotNull(user);
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightFirstName() {
        UserDTO userDTO = new UserDTO();
        String firstName = "test first name";
        userDTO.setFirstName(firstName);
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertEquals(firstName, user.getFirstName());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightLastName() {
        UserDTO userDTO = new UserDTO();
        String lastName = "test last name";
        userDTO.setLastName(lastName);
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertEquals(lastName, user.getLastName());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightPatronymic() {
        UserDTO userDTO = new UserDTO();
        String patronymic = "test patronymic";
        userDTO.setPatronymic(patronymic);
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertEquals(patronymic, user.getPatronymic());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightEmail() {
        UserDTO userDTO = new UserDTO();
        String email = "test email";
        userDTO.setEmail(email);
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertEquals(email, user.getEmail());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightPassword() {
        UserDTO userDTO = new UserDTO();
        String password = "test password";
        userDTO.setPassword(password);
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertEquals(password, user.getPassword());
    }

    @Test
    void shouldConvertUserToUserDTOAndGetNotNullObject() {
        User user = getTestUserWithRole();
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertNotNull(userDTO);
    }

    @Test
    void shouldConvertUserWithoutRoleToUserDTO() {
        User user = new User();
        assertThrows(RoleNotReceivedException.class, () -> userConverter.convertUserToUserDTO(user));
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightId() {
        User user = getTestUserWithRole();
        Long id = 1L;
        user.setId(id);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(id, userDTO.getId());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightUuid() {
        User user = getTestUserWithRole();
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        user.setUuid(uuid);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(uuid, userDTO.getUuid());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightFirstName() {
        User user = getTestUserWithRole();
        String firstName = "test first name";
        user.setFirstName(firstName);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(firstName, userDTO.getFirstName());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightLastName() {
        User user = getTestUserWithRole();
        String lastName = "test last name";
        user.setLastName(lastName);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(lastName, userDTO.getLastName());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightPatronymic() {
        User user = getTestUserWithRole();
        String patronymic = "test patronymic";
        user.setPatronymic(patronymic);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(patronymic, userDTO.getPatronymic());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightEmail() {
        User user = getTestUserWithRole();
        String email = "test email";
        user.setEmail(email);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(email, userDTO.getEmail());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightPassword() {
        User user = getTestUserWithRole();
        String password = "test password";
        user.setPassword(password);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(password, userDTO.getPassword());
    }

    @Test
    void shouldConvertUserToUserDTOAndGetRightRole() {
        User user = getTestUserWithRole();
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        Role role = user.getRole();
        RoleEnum roleEnum = role.getName();
        String stringRoleEnum = roleEnum.name();
        assertEquals(stringRoleEnum, userDTO.getRole().name());
    }

    private User getTestUserWithRole() {
        User user = new User();
        Role role = new Role();
        RoleEnum roleName = RoleEnum.ADMIN;
        role.setName(roleName);
        user.setRole(role);
        return user;
    }
}