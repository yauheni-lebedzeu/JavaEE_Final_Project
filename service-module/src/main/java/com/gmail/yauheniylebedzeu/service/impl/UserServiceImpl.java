package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.RoleRepository;
import com.gmail.yauheniylebedzeu.repository.UserContactsRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.repository.model.UserContacts;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.RoleNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.UserServiceException;
import com.gmail.yauheniylebedzeu.service.generator.RandomPasswordGenerator;
import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.service.util.ServiceUtil.*;

@Service
@AllArgsConstructor
@Log4j2
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserContactsRepository userContactsRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final RandomPasswordGenerator passwordGenerator;
    private final JavaMailSender javaMailSender;

    @Override
    @Transactional
    public UserDTO add(UserDTO userDTO) {
        User user = userConverter.convertUserDTOToUser(userDTO);
        String password = user.getPassword();
        password = passwordEncoder.encode(password);
        user.setPassword(password);
        RoleDTOEnum roleDTOEnum = userDTO.getRole();
        String roleName = roleDTOEnum.name();
        RoleEnum roleEnum = RoleEnum.valueOf(roleName);
        Optional<Role> optionalRole = roleRepository.findByName(roleEnum);
        if (optionalRole.isPresent()) {
            Role role = optionalRole.get();
            user.setRole(role);
        } else {
            throw new RoleNotFoundException(String.format("An unexpected error occurred while adding a user. Role" +
                    " named %s was not found in the database", roleName));
        }
        userRepository.persist(user);
        return userConverter.convertUserToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO findByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return userConverter.convertUserToUserDTOWithContacts(user);
        } else {
            throw new UserNotFoundException(String.format("User with email \"%s\" was not found in the database", email));
        }
    }

    @Override
    @Transactional
    public Long getCountOfUsers() {
        return userRepository.getCountOfEntities();
    }

    @Override
    @Transactional
    public UserDTO changePasswordByUuid(String uuid) {
        User user = userRepository.findByUuid(uuid);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException(String.format("User with uuid %s was not found in the database", uuid));
        } else {
            String randomPassword = passwordGenerator.getRandomPassword();
            String email = user.getEmail();
            sendPassword(randomPassword, email);
            String encodedPassword = passwordEncoder.encode(randomPassword);
            user.setPassword(encodedPassword);
            userRepository.merge(user);
            return userConverter.convertUserToUserDTO(user);
        }
    }

    @Override
    @Transactional
    public UserDTO changeRoleByUuid(String uuid, String roleName) {
        User user = userRepository.findByUuid(uuid);
        if (Objects.isNull(user)) {
            throw new UserNotFoundException(String.format("User with uuid %s was not found in the database", uuid));
        } else {
            RoleEnum roleEnum = RoleEnum.valueOf(roleName);
            Optional<Role> optionalRole = roleRepository.findByName(roleEnum);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                user.setRole(role);
                userRepository.merge(user);
                return userConverter.convertUserToUserDTO(user);
            } else {
                throw new RoleNotFoundException(String.format("An unexpected error occurred while changing a user" +
                        "role. Role named %s was not found in the database", roleName));
            }
        }
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        try {
            User user = userRepository.findByUuid(uuid);
            userRepository.remove(user);
        } catch (NoResultException e) {
            log.error(e.getMessage(), e);
            throw new UserNotFoundException(String.format("User with uuid %s was not found in the database", uuid));
        }
    }

    @Override
    @Transactional
    public PageDTO<UserDTO> getUserPage(int pageNumber, int pageSize, String sortParameter) {
        PageDTO<UserDTO> page = new PageDTO<>();
        Long countOfUsers = userRepository.getCountOfEntities();
        int countOfPages = getCountOfPages(countOfUsers, pageSize);
        page.setCountOfPages(countOfPages);
        if (pageNumber > countOfPages) {
            pageNumber = countOfPages;
        }
        page.setPageNumber(pageNumber);
        int startPosition = getStartPosition(pageNumber, pageSize);
        List<User> users = userRepository.findEntitiesWithLimit(startPosition, pageSize, sortParameter);
        List<UserDTO> userDTOs = userConverter.convertUserListToUserDTOList(users);
        List<UserDTO> usersOnPage = page.getObjects();
        usersOnPage.addAll(userDTOs);
        return page;
    }

    @Override
    @Transactional
    public UserDTO changeParameters(String uuid, UserUpdateDTO userUpdateDTO) {
        User user = userRepository.findByUuid(uuid);
        String firstName = userUpdateDTO.getFirstName();
        user.setFirstName(firstName);
        String lastName = userUpdateDTO.getLastName();
        user.setLastName(lastName);
        String password = userUpdateDTO.getPassword();
        if (StringUtils.isNotBlank(password)) {
            String encodedPassword = passwordEncoder.encode(password);
            user.setPassword(encodedPassword);
        }
        UserContacts contacts = user.getContacts();
        if (Objects.isNull(contacts)) {
            contacts = new UserContacts();
            contacts.setUser(user);
            userContactsRepository.persist(contacts);
        }
        String address = userUpdateDTO.getAddress();
        contacts.setAddress(address);
        String phoneNumber = userUpdateDTO.getPhoneNumber();
        contacts.setPhoneNumber(phoneNumber);
        userRepository.merge(user);
        return userConverter.convertUserToUserDTOWithContacts(user);
    }

    private void sendPassword(String randomPassword, String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("New password");
        mailMessage.setText(randomPassword);
        mailMessage.setFrom("LemotTest@yandex.ru");
        try {
            javaMailSender.send(mailMessage);
        } catch (MailException e) {
            log.error(e.getMessage(), e);
            throw new UserServiceException(String.format("Couldn't send password to the \"%s\"", email));
        }
    }
}
