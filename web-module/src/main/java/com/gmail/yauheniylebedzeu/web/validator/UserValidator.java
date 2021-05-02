package com.gmail.yauheniylebedzeu.web.validator;

import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Optional;

import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.*;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "patronymic", NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        UserDTO user = (UserDTO) object;
        String email = user.getEmail();
        if (StringUtils.isNotBlank(email)) {
            Optional<UserDTO> optionalUser = userService.getByEmail(email);
            if (optionalUser.isPresent()) {
                errors.rejectValue("email", USER_EXISTS_MESSAGE_PROPERTY);
            } else {
                if (!email.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,4})$")) {
                    errors.rejectValue("email", WRONG_EMAIL_FORMAT_MESSAGE_PROPERTY);
                }
                String password = user.getPassword();
                if (StringUtils.isNotBlank(password) && !password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$")) {
                    errors.rejectValue("password", WRONG_PASSWORD_FORMAT_MESSAGE_PROPERTY);
                }
                String firstName = user.getFirstName();
                if (StringUtils.isNotBlank(firstName) && !firstName.matches("^[A-Za-z]{1,20}$")) {
                    errors.rejectValue("firstName", WRONG_FIRST_NAME_FORMAT_MESSAGE_PROPERTY);
                }
                String lastName = user.getLastName();
                if (StringUtils.isNotBlank(lastName) && !lastName.matches("^[A-Za-z]{1,40}$")) {
                    errors.rejectValue("lastName", WRONG_LAST_NAME_FORMAT_MESSAGE_PROPERTY);
                }
                String patronymic = user.getPatronymic();
                if (StringUtils.isNotBlank(patronymic) && !patronymic.matches("^[A-Za-z]{1,40}$")) {
                    errors.rejectValue("patronymic", WRONG_PATRONYMIC_FORMAT_MESSAGE_PROPERTY);
                }
            }
        }
    }
}
