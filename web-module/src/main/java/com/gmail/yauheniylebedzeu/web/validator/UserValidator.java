package com.gmail.yauheniylebedzeu.web.validator;

import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundException;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant;
import lombok.AllArgsConstructor;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.*;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.*;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.*;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.PHONE_NUMBER_REGEXP_PATTERN;

@Component
@AllArgsConstructor
public class UserValidator implements Validator {

    private final UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserDTO.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, EMAIL_FIELD_NAME, NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PASSWORD_FIELD_NAME, NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIRST_NAME_FIELD_NAME, NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, LAST_NAME_FIELD_NAME, NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, PATRONYMIC_FIELD_NAME, NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        UserDTO user = (UserDTO) object;
        String email = user.getEmail();
        if (StringUtils.isNotBlank(email)) {
            if (email.matches(EMAIL_REGEX_PATTERN)) {
                try {
                    userService.findByEmail(email);
                    errors.rejectValue(EMAIL_FIELD_NAME, USER_EXISTS_MESSAGE_PROPERTY);
                } catch (UserNotFoundException e) {
                    String password = user.getPassword();
                    if (StringUtils.isNotBlank(password) && !password.matches(PASSWORD_REGEX_PATTERN)) {
                        errors.rejectValue(PASSWORD_FIELD_NAME, WRONG_PASSWORD_FORMAT_MESSAGE_PROPERTY);
                    }
                    String firstName = user.getFirstName();
                    if (StringUtils.isNotBlank(firstName) && !firstName.matches(ValidationConstant.FIRST_NAME_REGEX_PATTERN)) {
                        errors.rejectValue(FIRST_NAME_FIELD_NAME, WRONG_FIRST_NAME_FORMAT_MESSAGE_PROPERTY);
                    }
                    String lastName = user.getLastName();
                    if (StringUtils.isNotBlank(lastName) && !lastName.matches(ValidationConstant.LAST_NAME_AND_PATRONYMIC_REGEX_PATTERN)) {
                        errors.rejectValue(LAST_NAME_FIELD_NAME, WRONG_LAST_NAME_FORMAT_MESSAGE_PROPERTY);
                    }
                    String patronymic = user.getPatronymic();
                    if (StringUtils.isNotBlank(patronymic) && !patronymic.matches(ValidationConstant.LAST_NAME_AND_PATRONYMIC_REGEX_PATTERN)) {
                        errors.rejectValue(PATRONYMIC_FIELD_NAME, WRONG_PATRONYMIC_FORMAT_MESSAGE_PROPERTY);
                    }
                    String address = user.getAddress();
                    if (StringUtils.isNotBlank(address) && address.length() > ADDRESS_MAX_LENGTH) {
                        errors.rejectValue(ADDRESS_FIELD_NAME, WRONG_ADDRESS_FORMAT_MESSAGE_PROPERTY);
                    }
                    String phoneNumber = user.getPhoneNumber();
                    if (StringUtils.isNotBlank(phoneNumber) && !phoneNumber.matches(PHONE_NUMBER_REGEXP_PATTERN)) {
                        errors.rejectValue(PHONE_NUMBER_FIELD_NAME, WRONG_PHONE_NUMBER_FORMAT_MESSAGE_PROPERTY);
                    }
                }
            } else {
                errors.rejectValue(EMAIL_FIELD_NAME, WRONG_EMAIL_FORMAT_MESSAGE_PROPERTY);
            }
        }
    }
}
