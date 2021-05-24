package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.MailSendingService;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.PageDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;
import com.gmail.yauheniylebedzeu.web.validator.UserUpdateValidator;
import com.gmail.yauheniylebedzeu.web.validator.UserValidator;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADD_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADMIN_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CHANGE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CHANGE_PASSWORD_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CHANGE_ROLE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.DELETE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.PROFILE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getLoggedUserEmail;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getUserPrincipal;

@Controller
@AllArgsConstructor
@Log4j2
public class UserController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final UserUpdateValidator userUpdateValidator;
    private final MailSendingService mailSendingService;

    @GetMapping(value = ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL)
    public String getUsers(@RequestParam(defaultValue = "1") int pageNumber,
                           @RequestParam(defaultValue = "10") int pageSize, Model model) {
        PageDTO<UserDTO> page = userService.getUserPage(pageNumber, pageSize, "email");
        model.addAttribute("page", page);
        String email = getLoggedUserEmail();
        model.addAttribute("email", email);
        RoleDTOEnum[] allRoles = RoleDTOEnum.values();
        model.addAttribute("roles", allRoles);
        return "users";
    }

    @GetMapping(value = ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL + ADD_CONTROLLER_URL)
    public String getUserForm(UserDTO user, Model model, BindingResult errors) {
        RoleDTOEnum[] allRoles = RoleDTOEnum.values();
        model.addAttribute("roles", allRoles);
        return "user-form";
    }

    @PostMapping(value = ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL + ADD_CONTROLLER_URL)
    public String addUser(UserDTO user, Model model, BindingResult errors) {
        userValidator.validate(user, errors);
        if (errors.hasErrors()) {
            return getUserForm(user, model, errors);
        } else {
            userService.add(user);
            return "redirect:" + ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL;
        }
    }

    @PostMapping(value = ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL
            + CHANGE_PASSWORD_CONTROLLER_URL + "/{uuid}/{sourcePageNumber}")
    public String changePassword(@PathVariable String uuid,
                                 @PathVariable String sourcePageNumber) {
        UserDTO userWithUnencodedPassword = userService.changePasswordByUuid(uuid);
        mailSendingService.sendPasswordToUser(userWithUnencodedPassword);
        return "redirect:" + ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @PostMapping(value = ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL + DELETE_CONTROLLER_URL + "/{sourcePageNumber}")
    public String delUsers(@PathVariable String sourcePageNumber,
                           @RequestParam List<String> uuids) {
        uuids.forEach(userService::removeByUuid);
        return "redirect:" + ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @PostMapping(value = ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL + CHANGE_ROLE_CONTROLLER_URL + "/{uuid}/{sourcePageNumber}")
    public String changeRole(@PathVariable String uuid,
                             @PathVariable String sourcePageNumber,
                             @RequestParam String roleName) {
        try {
            RoleDTOEnum.valueOf(roleName);
            userService.changeRoleByUuid(uuid, roleName);
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage(), e);
            throw new IllegalArgumentException(String.format("A role named %s does not exist", roleName), e);
        }
        return "redirect:" + ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL + "?pageNumber=" + sourcePageNumber;
    }

    @GetMapping(value = CUSTOMER_CONTROLLER_URL + PROFILE_CONTROLLER_URL)
    public String GetProfile(UserUpdateDTO userUpdateDTO, BindingResult errors, Model model) {
        String email = getLoggedUserEmail();
        UserDTO user = userService.findByEmail(email);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping(value = CUSTOMER_CONTROLLER_URL + PROFILE_CONTROLLER_URL + CHANGE_CONTROLLER_URL + "/{uuid}")
    public String changeUserParameters(@PathVariable String uuid, UserUpdateDTO userUpdateDTO,
                                       BindingResult errors, Model model) {
        userUpdateValidator.validate(userUpdateDTO, errors);
        if (errors.hasErrors()) {
            return GetProfile(userUpdateDTO, errors, model);
        } else {
            userService.changeParameters(uuid, userUpdateDTO);
            return "redirect:" + CUSTOMER_CONTROLLER_URL + PROFILE_CONTROLLER_URL;
        }
    }
}