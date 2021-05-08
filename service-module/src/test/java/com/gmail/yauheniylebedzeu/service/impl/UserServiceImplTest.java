package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.RoleRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.RoleIsNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.generator.RandomPasswordGenerator;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private UserConverter userConverter;
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private RandomPasswordGenerator passwordGenerator;
    @Mock
    private JavaMailSender javaMailSender;
    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldAddUserWithRoleAndGetUserWithId() {
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.ADMIN;
        userDTO.setRole(roleDTOEnum);
        String password = "test password";
        userDTO.setPassword(password);
        User user = new User();
        user.setPassword(password);
        when(userConverter.convertUserDTOToUser(userDTO)).thenReturn(user);
        String encodedPassword = "test encoded password";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        RoleEnum roleEnum = RoleEnum.valueOf(roleDTOEnum.name());
        Role role = new Role();
        role.setName(roleEnum);
        when(roleRepository.findByName(roleEnum)).thenReturn(Optional.of(role));
        user.setRole(role);
        Long userId = 1L;
        user.setId(userId);
        userDTO.setId(userId);
        when(userConverter.convertUserToUserDTO(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.add(userDTO);
        assertEquals(userId, resultUserDTO.getId());
    }

    @Test
    void shouldAddUserWithRoleAndGetEmptyOptionalRole() {
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.ADMIN;
        userDTO.setRole(roleDTOEnum);
        String password = "test password";
        userDTO.setPassword(password);
        User user = new User();
        user.setPassword(password);
        when(userConverter.convertUserDTOToUser(userDTO)).thenReturn(user);
        String encodedPassword = "test encoded password";
        when(passwordEncoder.encode(password)).thenReturn(encodedPassword);
        RoleEnum roleEnum = RoleEnum.valueOf(roleDTOEnum.name());
        when(roleRepository.findByName(roleEnum)).thenReturn(Optional.empty());
        assertThrows(RoleIsNotFoundException.class, () -> userService.add(userDTO));
    }

    @Test
    void shouldFindUserByEmailAndGetEmptyOptionalObject() {
        String email = "Uaer@email.ru";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(email));
    }

    @Test
    void shouldGetCountOfUsers() {
        Long countOfUsers = 5L;
        when(userRepository.getCountOfEntities()).thenReturn(countOfUsers);
        Long resultCountOfUsers = userService.getCountOfUsers();
        assertEquals(countOfUsers, resultCountOfUsers);
    }

    @Test
    void shouldChangeUserRoleByUuidAndGetUserDTOWithNewRole() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRole();
        user.setUuid(uuid);
        when(userRepository.findByUuid(uuid)).thenReturn(user);
        String newRoleName = RoleDTOEnum.CUSTOMER_USER.name();
        RoleEnum newRoleEnum = RoleEnum.valueOf(newRoleName);
        Role newRole = new Role();
        newRole.setName(newRoleEnum);
        when(roleRepository.findByName(newRoleEnum)).thenReturn(Optional.of(newRole));
        user.setRole(newRole);
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(newRoleEnum.name());
        userDTO.setRole(roleDTOEnum);
        when(userConverter.convertUserToUserDTO(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changeRoleByUuid(uuid, newRoleName);
        String resultRoleName = resultUserDTO.getRole().name();
        assertEquals(newRoleName, resultRoleName);
    }

    @Test
    void shouldChangeUserRoleByUuidAndGetNullUser() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRole();
        user.setUuid(uuid);
        when(userRepository.findByUuid(uuid)).thenReturn(null);
        String newRoleName = RoleDTOEnum.CUSTOMER_USER.name();
        assertThrows(UserNotFoundException.class, () -> userService.changeRoleByUuid(uuid, newRoleName));
    }

    @Test
    void shouldChangeUserRoleByUuidAndGetEmptyOptionalRole() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRole();
        user.setUuid(uuid);
        when(userRepository.findByUuid(uuid)).thenReturn(user);
        String newRoleName = RoleDTOEnum.CUSTOMER_USER.name();
        RoleEnum newRoleEnum = RoleEnum.valueOf(newRoleName);
        Role newRole = new Role();
        newRole.setName(newRoleEnum);
        when(roleRepository.findByName(newRoleEnum)).thenReturn(Optional.empty());
        assertThrows(RoleIsNotFoundException.class, () -> userService.changeRoleByUuid(uuid, newRoleName));
    }

    @Test
    void shouldChangePasswordByUuidAndGetUserDTOWithNewPassword() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRole();
        when(userRepository.findByUuid(uuid)).thenReturn(user);
        String newPassword = "test password";
        when(passwordGenerator.getRandomPassword()).thenReturn(newPassword);
        String newEncodedPassword = "test encoded password";
        when(passwordEncoder.encode(newPassword)).thenReturn(newEncodedPassword);
        user.setPassword(newEncodedPassword);
        RoleEnum roleEnum = user.getRole().getName();
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleEnum.name());
        userDTO.setRole(roleDTOEnum);
        userDTO.setPassword(newEncodedPassword);
        when(userConverter.convertUserToUserDTO(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changePasswordByUuid(uuid);
        String resultPassword = resultUserDTO.getPassword();
        assertEquals(newEncodedPassword, resultPassword);
    }

    @Test
    void shouldChangePasswordByUuidAndGetNullUser() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        when(userRepository.findByUuid(uuid)).thenReturn(null);
        assertThrows(UserNotFoundException.class, () -> userService.changePasswordByUuid(uuid));
    }

    private User getUserWithTestRole() {
        User user = new User();
        Role role = new Role();
        role.setName(RoleEnum.ADMIN);
        user.setRole(role);
        return user;
    }
}