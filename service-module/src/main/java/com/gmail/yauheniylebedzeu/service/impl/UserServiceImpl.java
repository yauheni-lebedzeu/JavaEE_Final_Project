package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.RoleRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserDeletedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.generator.RandomPasswordGenerator;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.service.util.EntitiesServiceUtil.getUserContacts;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.checkPageNumber;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getCountOfPages;
import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.getStartPosition;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final RandomPasswordGenerator passwordGenerator;

    @Override
    @Transactional
    public UserDTO add(UserDTO userDTO) {
        User user = userConverter.convertUserDTOToUser(userDTO);
        String password = user.getPassword();
        password = passwordEncoder.encode(password);
        user.setPassword(password);
        RoleDTOEnum roleDTOEnum = userDTO.getRole();
        String roleName = roleDTOEnum.name();
        Role role = getSafeRole(roleName);
        user.setRole(role);
        userRepository.persist(user);
        return userConverter.convertUserToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            if (user.getIsDeleted()) {
                throw new UserDeletedModuleException(String.format("User with email %s was deleted from database", email));
            }
            return userConverter.convertUserToUserDTOWithContacts(user);
        } else {
            throw new UserNotFoundModuleException(String.format("User with email \"%s\" was not found in the database", email));
        }
    }

    @Override
    @Transactional
    public Long getCountOfUsers() {
        return userRepository.getCountOfEntities();
    }

    @Override
    @Transactional
    public UserDTO changeRoleByUuid(String uuid, String roleName) {
        User user = getSafeUser(uuid);
        Role role = getSafeRole(roleName);
        user.setRole(role);
        userRepository.merge(user);
        return userConverter.convertUserToUserDTO(user);
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        User user = getSafeUser(uuid);
        userRepository.remove(user);

    }

    @Override
    @Transactional
    public PageDTO<UserDTO> getUserPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<UserDTO> page = new PageDTO<>();
        Long countOfUsers = userRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfUsers, pageSize);
        page.setCountOfPages(countOfPages);
        pageNumber = checkPageNumber(pageNumber, countOfPages);
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<User> users = userRepository.findEntitiesWithLimits(startPosition, pageSize, sortParameter);
        List<UserDTO> userDTOs = userConverter.convertUserListToUserDTOList(users);
        List<UserDTO> usersOnPage = page.getObjects();
        usersOnPage.addAll(userDTOs);
        return page;
    }

    @Override
    @Transactional
    public UserDTO changeParameters(String uuid, UserUpdateDTO userUpdateDTO) {
        User user = getSafeUser(uuid);
        String firstName = userUpdateDTO.getFirstName();
        user.setFirstName(firstName);
        String lastName = userUpdateDTO.getLastName();
        user.setLastName(lastName);
        String password = userUpdateDTO.getPassword();
        if (StringUtils.isNotBlank(password)) {
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }
        String address = userUpdateDTO.getAddress();
        String phoneNumber = userUpdateDTO.getPhoneNumber();
        if (StringUtils.isNotBlank(address) || StringUtils.isNotBlank(phoneNumber)) {
            UserContacts contacts = getUserContacts(user);
            contacts.setAddress(address);
            contacts.setPhoneNumber(phoneNumber);
        }
        userRepository.merge(user);
        return userConverter.convertUserToUserDTOWithContacts(user);

    }

    @Override
    @Transactional
    public UserDTO changePasswordByUuid(String uuid) {
        User user = getSafeUser(uuid);
        String randomPassword = passwordGenerator.getRandomPassword();
        String encodedPassword = passwordEncoder.encode(randomPassword);
        user.setPassword(encodedPassword);
        userRepository.merge(user);
        UserDTO userDTO = userConverter.convertUserToUserDTO(user);
        userDTO.setPassword(randomPassword);
        return userDTO;

    }

    @Override
    @Transactional
    public List<UserDTO> findAll() {
        List<User> users = userRepository.findAll();
        return userConverter.convertUserListToUserDTOList(users);
    }

    @Override
    public User getSafeUser(String userUuid) {
        Optional<User> optionalUser = userRepository.findByUuid(userUuid);
        if (optionalUser.isPresent()) {
            return optionalUser.get();
        } else {
            throw new UserNotFoundModuleException(String.format("User with uuid %s was not found", userUuid));
        }
    }

    @Override
    @Transactional
    public UserDTO restore(String userUuid) {
        User user = getSafeUser(userUuid);
        UserContacts contacts = user.getContacts();
        contacts.setIsDeleted(false);
        user.setIsDeleted(false);
        userRepository.merge(user);
        return userConverter.convertUserToUserDTO(user);
    }

    protected Role getSafeRole(String roleName) {
        RoleEnum roleEnum = RoleEnum.valueOf(roleName);
        Optional<Role> optionalRole = roleRepository.findByName(roleEnum);
        if (optionalRole.isPresent()) {
            return optionalRole.get();
        } else {
            throw new RoleNotFoundModuleException(String.format("An unexpected error occurred while adding a user. Role" +
                    " named %s was not found", roleEnum.name()));
        }
    }
}