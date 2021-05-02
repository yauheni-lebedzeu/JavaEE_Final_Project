package com.gmail.yauheniylebedzeu.service.impl;

import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.exception.EmailNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserLogin;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserDTO> optionalUser = userService.getByEmail(username);
        if (optionalUser.isPresent()) {
            UserDTO user = optionalUser.get();
            return new UserLogin(user);
        } else {
            throw new EmailNotFoundException("A user with email " + username + " was not found in the database");
        }
    }
}
