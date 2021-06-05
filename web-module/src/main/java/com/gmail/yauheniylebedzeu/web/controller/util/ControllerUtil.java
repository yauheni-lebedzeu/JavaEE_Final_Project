package com.gmail.yauheniylebedzeu.web.controller.util;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserLogin;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public class ControllerUtil {

    public static Optional<UserDTO> getUserPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserLogin userLogin = (UserLogin) authentication.getPrincipal();
            UserDTO userPrincipal = userLogin.getUser();
            return Optional.of(userPrincipal);
        } else {
            return Optional.empty();
        }
    }

    public static String getLoggedUserUuid() {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        UserDTO loggedInUser = optionalUser.get();
        return loggedInUser.getUuid();
    }

    public static String getLoggedUserRoleName() {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        UserDTO loggedInUser = optionalUser.get();
        RoleDTOEnum role = loggedInUser.getRole();
        return role.name();
    }

    public static String getLoggedUserEmail() {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        UserDTO loggedInUser = optionalUser.get();
        return loggedInUser.getEmail();
    }

}
