package com.gmail.yauheniylebedzeu.web.controller.api;

import com.gmail.yauheniylebedzeu.repository.model.User;
import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.converter.BindingResultConverter;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.web.validator.UserValidator;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;


@RestController
@RequestMapping(value = API_CONTROLLER_URL)
@Lazy
@AllArgsConstructor
public class UserAPIController {

    private final UserService userService;
    private final UserValidator userValidator;
    private final BindingResultConverter bindingResultConverter;

    @PostMapping(value = USERS_CONTROLLER_URL)
    public ResponseEntity<?> addUser(@RequestBody UserDTO user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            ErrorsDTO errors = bindingResultConverter.convertBindingResultToErrorsDTO(bindingResult);
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }
        userService.add(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(value = USERS_CONTROLLER_URL)
    public List<User> getUsers() {
        return Collections.emptyList();
    }
}
