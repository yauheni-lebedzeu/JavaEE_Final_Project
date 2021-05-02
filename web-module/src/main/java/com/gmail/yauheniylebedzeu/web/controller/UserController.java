package com.gmail.yauheniylebedzeu.web.controller;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserLogin;
import com.gmail.yauheniylebedzeu.web.controller.exception.UserControllerException;
import com.gmail.yauheniylebedzeu.web.validator.UserValidator;
import lombok.AllArgsConstructor;
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
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;

    @GetMapping(value = "/users/{pageNumber}/{pageSize}")
    public String getUserList(@PathVariable int pageNumber,
                              @PathVariable int pageSize,
                              Model model) {
        Long countOfUsers = userService.getCountOfUsers();
        int startPosition = (pageNumber - 1) * pageSize;
        List<UserDTO> users = userService.findAll(startPosition, pageSize, "email");
        int countOfPages = (int) (Math.ceil(countOfUsers / (double) pageSize));
        model.addAttribute("users", users);
        model.addAttribute("countOfPages", countOfPages);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserLogin userLogin = (UserLogin) authentication.getPrincipal();
            UserDTO user = userLogin.getUser();
            String email = user.getEmail();
            model.addAttribute("email", email);
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
            return "redirect:/users/1/10";
        }
    }

    @PostMapping(value = "/users/change-password/{uuid}")
    public String changePassword(@PathVariable String uuid) {
        userService.changePasswordByUuid(uuid);
        return "redirect:/users/1/10";
    }

    @PostMapping(value = "/users/del")
    public String delUsers(@RequestParam(required = false) List<String> uuids) {
        if (uuids != null) {
            uuids.forEach(userService::removeByUuid);
        }
        return "redirect:/users/1/10";
    }

    @PostMapping(value="/users/change-role/{uuid}")
    public String changeRole(@PathVariable String uuid,
                             @RequestParam(name="role_name", required = false) String roleName) {
        if (StringUtils.isNotBlank(roleName)) {
            try {
                RoleDTOEnum.valueOf(roleName);
                userService.changeRoleByUuid(uuid, roleName);
            } catch (IllegalArgumentException e) {
                throw new UserControllerException(String.format("A role named %s does not exist", roleName), e);
            }
        }
        return "redirect:/users/1/10";
    }
}
