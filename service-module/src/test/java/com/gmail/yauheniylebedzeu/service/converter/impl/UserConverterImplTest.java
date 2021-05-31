package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserConverterImplTest {

    private final UserConverter userConverter;

    UserConverterImplTest() {
        userConverter = new UserConverterImpl();
    }

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
    void shouldConvertUserDTOToUserAndReturnNotNullContacts() {
        UserDTO userDTO = new UserDTO();
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertNotNull(user.getContacts());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightAddress() {
        UserDTO userDTO = new UserDTO();
        String address = "test address";
        userDTO.setAddress(address);
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertEquals(address, user.getContacts().getAddress());
    }

    @Test
    void shouldConvertUserDTOToUserAndReturnRightPhoneNumber() {
        UserDTO userDTO = new UserDTO();
        String phoneNumber = "11111111111";
        userDTO.setPhoneNumber(phoneNumber);
        User user = userConverter.convertUserDTOToUser(userDTO);
        assertEquals(phoneNumber, user.getContacts().getPhoneNumber());
    }

    @Test
    void shouldConvertUserToUserDTOAndGetNotNullObject() {
        User user = getTestUserWithValidRole();
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertNotNull(userDTO);
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightId() {
        User user = getTestUserWithValidRole();
        Long id = 1L;
        user.setId(id);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(id, userDTO.getId());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightUuid() {
        User user = getTestUserWithValidRole();
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        user.setUuid(uuid);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(uuid, userDTO.getUuid());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightFirstName() {
        User user = getTestUserWithValidRole();
        String firstName = "test first name";
        user.setFirstName(firstName);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(firstName, userDTO.getFirstName());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightLastName() {
        User user = getTestUserWithValidRole();
        String lastName = "test last name";
        user.setLastName(lastName);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(lastName, userDTO.getLastName());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightPatronymic() {
        User user = getTestUserWithValidRole();
        String patronymic = "test patronymic";
        user.setPatronymic(patronymic);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(patronymic, userDTO.getPatronymic());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightEmail() {
        User user = getTestUserWithValidRole();
        String email = "test email";
        user.setEmail(email);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(email, userDTO.getEmail());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightPassword() {
        User user = getTestUserWithValidRole();
        String password = "test password";
        user.setPassword(password);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(password, userDTO.getPassword());
    }

    @Test
    void shouldConvertUserToUserDTOAndReturnRightIsDeleted() {
        User user = getTestUserWithValidRole();
        user.setIsDeleted(false);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        assertEquals(false, userDTO.getIsDeleted());
    }

    @Test
    void shouldConvertUserToUserDTOAndGetRightRole() {
        User user = getTestUserWithValidRole();
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        Role role = user.getRole();
        RoleEnum roleEnum = role.getName();
        String stringRoleEnum = roleEnum.name();
        assertEquals(stringRoleEnum, userDTO.getRole().name());
    }

    @Test
    void shouldConvertUserToUserDTOWithContactsAndGetNotNullObject() {
        User user = getTestUserWithValidRole();
        UserContacts userContacts = new UserContacts();
        user.setContacts(userContacts);
        UserDTO userDTO = userConverter.convertUserToUserDTOWithContacts(user);
        assertNotNull(userDTO);
    }

    @Test
    void shouldConvertUserToUserDTOWithContactsAndReturnRightAddress() {
        User user = getTestUserWithValidRole();
        UserContacts userContacts = new UserContacts();
        String address = "Test address";
        userContacts.setAddress(address);
        user.setContacts(userContacts);
        UserDTO userDTO = userConverter.convertUserToUserDTOWithContacts(user);
        assertEquals(address, userDTO.getAddress());
    }

    @Test
    void shouldConvertUserToUserDTOWithContactsAndReturnRightPhoneNumber() {
        User user = getTestUserWithValidRole();
        UserContacts userContacts = new UserContacts();
        String phoneNumber = "+1111111111";
        userContacts.setPhoneNumber(phoneNumber);
        user.setContacts(userContacts);
        UserDTO userDTO = userConverter.convertUserToUserDTOWithContacts(user);
        assertEquals(phoneNumber, userDTO.getPhoneNumber());
    }

    @Test
    void shouldConvertEmptyUserListToUserDTOList() {
        List<User> users = Collections.emptyList();
        List<UserDTO> userDTOs = userConverter.convertUserListToUserDTOList(users);
        assertTrue(userDTOs.isEmpty());
    }

    @Test
    void shouldConvertUserListToUserDTOList() {
        User user = getTestUserWithValidRole();
        List<User> users = Collections.singletonList(user);
        List<UserDTO> userDTOs = userConverter.convertUserListToUserDTOList(users);
        assertEquals(1, userDTOs.size());
    }

    private User getTestUserWithValidRole() {
        User user = new User();
        Role role = new Role();
        RoleEnum roleName = RoleEnum.ADMIN;
        role.setName(roleName);
        user.setRole(role);
        return user;
    }
}