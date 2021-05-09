package com.gmail.yauheniylebedzeu.web.controller;

import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserLogin;
import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;
import com.gmail.yauheniylebedzeu.web.controller.exception.UserControllerException;
import com.gmail.yauheniylebedzeu.web.validator.UserUpdateValidator;
import com.gmail.yauheniylebedzeu.web.validator.UserValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@AllArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final UserUpdateValidator userUpdateValidator;

    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(defaultValue = "1") int pageNumber, Model model) {
        int pageSize = 10;
        PageDTO<UserDTO> page = userService.getUserPage(pageNumber, pageSize, "email");
        model.addAttribute("page", page);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserLogin userLogin = (UserLogin) authentication.getPrincipal();
            UserDTO user = userLogin.getUser();
            String email = user.getEmail();
            model.addAttribute("email", email);
        } else {
            return "redirect:/login";
        }
        RoleDTOEnum[] allRoles = RoleDTOEnum.values();
        model.addAttribute("allRoles", allRoles);
        return "users";
    }

    @GetMapping(value = "/users/add")
    public String getUserForm(UserDTO user, Model model, BindingResult errors) {
        RoleDTOEnum[] allRoles = RoleDTOEnum.values();
        model.addAttribute("allRoles", allRoles);
        return "user-form";
    }

    @PostMapping(value = "/users/add")
    public String addUser(UserDTO user, Model model, BindingResult errors) {
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            return getUserForm(user, model, errors);
        } else {
            userService.add(user);
            return "redirect:/users";
        }
    }

    @PostMapping(value = "/users/change-password/{uuid}")
    public String changePassword(@PathVariable String uuid) {
        userService.changePasswordByUuid(uuid);
        return "redirect:/users";
    }

    @PostMapping(value = "/users/del/{pageNumber}")
    public String delUsers(@PathVariable String pageNumber, @RequestParam(required = false) List<String> uuids) {
        if (uuids != null) {
            uuids.forEach(userService::removeByUuid);
        }
        return "redirect:/users?pageNumber=" + pageNumber;
    }

    @PostMapping(value = "/users/change-role/{uuid}")
    public String changeRole(@PathVariable String uuid,
                             @RequestParam(name = "role_name", required = false) String roleName) {
        if (StringUtils.isNotBlank(roleName)) {
            try {
                RoleDTOEnum.valueOf(roleName);
                userService.changeRoleByUuid(uuid, roleName);
            } catch (IllegalArgumentException e) {
                log.error(e.getMessage(), e);
                throw new UserControllerException(String.format("A role named %s does not exist", roleName), e);
            }
        }
        return "redirect:/users";
    }

    @GetMapping(value = "/users/user/profile")
    public String GetProfile(UserUpdateDTO userUpdateDTO, BindingResult errors, Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserLogin userLogin = (UserLogin) authentication.getPrincipal();
            UserDTO loggedInUser = userLogin.getUser();
            String email = loggedInUser.getEmail();
            UserDTO user = userService.findByEmail(email);
            model.addAttribute("user", user);
        } else {
            return "redirect:/login";
        }
        return "profile";
    }

    @PostMapping(value = "/users/user/change/{uuid}")
    public String changeUserParameters(@PathVariable String uuid, UserUpdateDTO userUpdateDTO,
                                       BindingResult errors, Model model) {
        userUpdateValidator.validate(userUpdateDTO, errors);
        if (errors.hasErrors()) {
            return GetProfile(userUpdateDTO, errors, model);
        } else {
            userService.changeParameters(uuid, userUpdateDTO);
            return "redirect:/users/user/profile";
        }
    }
}
