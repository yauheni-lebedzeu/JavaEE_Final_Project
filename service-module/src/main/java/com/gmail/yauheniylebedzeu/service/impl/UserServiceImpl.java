package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.repository.RoleRepository;
import com.gmail.yauheniylebedzeu.repository.UserRepository;
import com.gmail.yauheniylebedzeu.repository.enums.RoleEnum;
import com.gmail.yauheniylebedzeu.repository.model.Role;
import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.UserConverter;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.exception.UserServiceException;
import com.gmail.yauheniylebedzeu.service.generator.RandomPasswordGenerator;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserConverter userConverter;
    private final PasswordEncoder passwordEncoder;
    private final RandomPasswordGenerator passwordGenerator;
    private final JavaMailSender javaMailSender;

    @Override
    @Transactional
    public UserDTO add(UserDTO userDTO) {
        User user = userConverter.convertUserDTOtoUser(userDTO);
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
            throw new UserServiceException("An unexpected error occurred while adding a user. Role named "
                    + roleName + " was not found in the database");
        }
        userRepository.persist(user);
        return userConverter.convertUserToUserDTO(user);
    }

    @Override
    @Transactional
    public UserDTO getByEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            return userConverter.convertUserToUserDTO(user);
        } else {
            throw new UserNotFoundException("User wit email " + email + " was not found in the database");
        }
    }

    @Override
    @Transactional
    public Long getCountOfUsers() {
        return userRepository.getCountOfEntities();
    }

    @Override
    @Transactional
    public List<UserDTO> findAll(int startPosition, int maxResult, String sortFieldName) {
        List<User> users = userRepository.findAll(startPosition, maxResult, sortFieldName);
        return users.stream()
                .map(userConverter::convertUserToUserDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<UserDTO> changePasswordByUuid(String uuid) {
        Optional<User> optionalUser = userRepository.findByUuid(uuid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            String randomPassword = passwordGenerator.getRandomPassword();
            String email = user.getEmail();

            sendPassword(randomPassword, email);

            String encodedPassword = passwordEncoder.encode(randomPassword);
            user.setPassword(encodedPassword);
            userRepository.merge(user);
            UserDTO userDTO = userConverter.convertUserToUserDTO(user);
            return Optional.ofNullable(userDTO);
        } else {
            return Optional.empty();
        }
    }

    @Override
    @Transactional
    public Optional<UserDTO> changeRoleByUuid(String uuid, String roleName) {
        Optional<User> optionalUser = userRepository.findByUuid(uuid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            RoleEnum roleEnum = RoleEnum.valueOf(roleName);
            Optional<Role> optionalRole = roleRepository.findByName(roleEnum);
            if (optionalRole.isPresent()) {
                Role role = optionalRole.get();
                user.setRole(role);
                userRepository.merge(user);
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public void removeByUuid(String uuid) {
        Optional<User> optionalUser = userRepository.findByUuid(uuid);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            userRepository.remove(user);
        }
    }

    private void sendPassword(String randomPassword, String email) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(email);
        mailMessage.setSubject("New password");
        mailMessage.setText(randomPassword);
        mailMessage.setFrom("LemotTest@yandex.ru");
        javaMailSender.send(mailMessage);
    }
}
