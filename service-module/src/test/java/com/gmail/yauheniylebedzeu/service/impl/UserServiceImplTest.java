package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.RoleRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserContactsNotReceivedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserDeletedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.generator.RandomPasswordGenerator;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    void shouldAddUserWithRoleAndNotFindRole() {
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
        assertThrows(RoleNotFoundModuleException.class, () -> userService.add(userDTO));
    }

    @Test
    void shouldFindUserByEmailAndGetNotDeletedUser() {
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setIsDeleted(false);
        String email = "User@email.ru";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        UserDTO userDTO = new UserDTO();
        userDTO.setRole(RoleDTOEnum.ADMIN);
        userDTO.setEmail(email);
        when(userConverter.convertUserToUserDTOWithContacts(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.findByEmail(email);
        assertEquals(email, resultUserDTO.getEmail());
    }

    @Test
    void shouldFindUserByEmailAndGetDeletedUser() {
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setIsDeleted(true);
        String email = "User@email.ru";
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        assertThrows(UserDeletedModuleException.class, () ->  userService.findByEmail(email));
    }

    @Test
    void shouldFindUserByEmailAndNotFindSuchUser() {
        String email = "User@email.ru";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundModuleException.class, () -> userService.findByEmail(email));
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
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setUuid(uuid);
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
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
    void shouldChangeUserRoleByUuidAndNotFindUser() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setUuid(uuid);
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        String newRoleName = RoleDTOEnum.CUSTOMER_USER.name();
        assertThrows(UserNotFoundModuleException.class, () -> userService.changeRoleByUuid(uuid, newRoleName));
    }

    @Test
    void shouldChangeUserRoleByUuidAndNotFindRole() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = new User();
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        String newRoleName = RoleDTOEnum.CUSTOMER_USER.name();
        RoleEnum newRoleEnum = RoleEnum.valueOf(newRoleName);
        Role newRole = new Role();
        newRole.setName(newRoleEnum);
        when(roleRepository.findByName(newRoleEnum)).thenReturn(Optional.empty());
        assertThrows(RoleNotFoundModuleException.class, () -> userService.changeRoleByUuid(uuid, newRoleName));
    }

    @Test
    void shouldRemoveUserByUuidAndNotFindUser() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundModuleException.class, () -> userService.removeByUuid(uuid));
    }

    @Test
    void shouldChangePasswordByUuidAndGetUserDTOWithNewUnencodedPassword() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
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
        userDTO.setPassword(newPassword);
        UserDTO resultUserDTO = userService.changePasswordByUuid(uuid);
        String resultPassword = resultUserDTO.getPassword();
        assertEquals(newPassword, resultPassword);
    }

    @Test
    void shouldChangePasswordByUuidAndGetEmptyOptional() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundModuleException.class, () -> userService.changePasswordByUuid(uuid));
    }

    @Test
    void shouldChangeUserParametersAndNotFindThisUser() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        assertThrows(UserNotFoundModuleException.class, () -> userService.changeParameters(uuid, userUpdateDTO));
    }

    @Test
    void shouldChangeUserFirstNameAndGetUserDTOWithNewFirstName() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setFirstName("old first name");
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        String newFirstName = "new first name";
        userUpdateDTO.setFirstName(newFirstName);
        RoleEnum roleEnum = user.getRole().getName();
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleEnum.name());
        userDTO.setRole(roleDTOEnum);
        userDTO.setFirstName(newFirstName);
        when(userConverter.convertUserToUserDTOWithContacts(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changeParameters(uuid, userUpdateDTO);
        assertEquals(newFirstName, resultUserDTO.getFirstName());
    }

    @Test
    void shouldChangeUserLastNameAndGetUserDTOWithNewLastName() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setLastName("old last name");
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        String newLastName = "new last name";
        userUpdateDTO.setLastName(newLastName);
        RoleEnum roleEnum = user.getRole().getName();
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleEnum.name());
        userDTO.setRole(roleDTOEnum);
        userDTO.setLastName(newLastName);
        when(userConverter.convertUserToUserDTOWithContacts(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changeParameters(uuid, userUpdateDTO);
        assertEquals(newLastName, resultUserDTO.getLastName());
    }

    @Test
    void shouldChangeUserAddressAndGetUserDTOWithNewAddress() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        user.getContacts().setAddress("old address");
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        String newAddress = "new address";
        userUpdateDTO.setAddress(newAddress);
        RoleEnum roleEnum = user.getRole().getName();
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleEnum.name());
        userDTO.setRole(roleDTOEnum);
        userDTO.setAddress(newAddress);
        when(userConverter.convertUserToUserDTOWithContacts(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changeParameters(uuid, userUpdateDTO);
        assertEquals(newAddress, resultUserDTO.getAddress());
    }

    @Test
    void shouldChangeUserPhoneNumberAndGetUserDTOWithNewPhoneNumber() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        user.getContacts().setPhoneNumber("+1111111111");
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        String newPhoneNumber = "+2222222222";
        userUpdateDTO.setPhoneNumber(newPhoneNumber);
        RoleEnum roleEnum = user.getRole().getName();
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleEnum.name());
        userDTO.setRole(roleDTOEnum);
        userDTO.setPhoneNumber(newPhoneNumber);
        when(userConverter.convertUserToUserDTOWithContacts(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changeParameters(uuid, userUpdateDTO);
        assertEquals(newPhoneNumber, resultUserDTO.getPhoneNumber());
    }

    @Test
    void shouldChangeUserContactsAndGetNullContacts() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setContacts(null);
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        String newAddress = "new address";
        userUpdateDTO.setAddress(newAddress);
        assertThrows(UserContactsNotReceivedModuleException.class, () -> userService.changeParameters(uuid, userUpdateDTO));
    }

    @Test
    void shouldChangeUserContactsAndGetUserDTOWithSameContacts() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        String oldAddress = "old address";
        user.getContacts().setAddress(oldAddress);
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        RoleEnum roleEnum = user.getRole().getName();
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleEnum.name());
        userDTO.setRole(roleDTOEnum);
        userDTO.setAddress(oldAddress);
        when(userConverter.convertUserToUserDTOWithContacts(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changeParameters(uuid, userUpdateDTO);
        assertEquals(oldAddress, resultUserDTO.getAddress());
    }

    @Test
    void shouldChangeUserPasswordAndGetUserDTOWithNewPassword() {
        String uuid = "6d4883c7-aa9c-11eb-a3ca-0242ac130002";
        User user = getUserWithTestRoleAndEmptyContacts();
        user.setPassword("old password");
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(user));
        UserUpdateDTO userUpdateDTO = new UserUpdateDTO();
        String newPassword = "new password";
        userUpdateDTO.setPassword(newPassword);
        RoleEnum roleEnum = user.getRole().getName();
        UserDTO userDTO = new UserDTO();
        RoleDTOEnum roleDTOEnum = RoleDTOEnum.valueOf(roleEnum.name());
        userDTO.setRole(roleDTOEnum);
        userDTO.setPassword(newPassword);
        when(userConverter.convertUserToUserDTOWithContacts(user)).thenReturn(userDTO);
        UserDTO resultUserDTO = userService.changeParameters(uuid, userUpdateDTO);
        assertEquals(newPassword, resultUserDTO.getPassword());
    }

    private User getUserWithTestRoleAndEmptyContacts() {
        User user = new User();
        Role role = new Role();
        role.setName(RoleEnum.ADMIN);
        user.setRole(role);
        UserContacts contacts = new UserContacts();
        user.setContacts(contacts);
        return user;
    }
}