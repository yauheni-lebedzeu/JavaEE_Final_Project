package com.gmail.yauheniylebedzeu.web.validator;

import com.gmail.yauheniylebedzeu.service.UserService;
import com.gmail.yauheniylebedzeu.service.exception.UserDeletedModuleException;
import com.gmail.yauheniylebedzeu.service.exception.UserNotFoundModuleException;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import lombok.AllArgsConstructor;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.ADDRESS_FIELD_NAME;
import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.EMAIL_FIELD_NAME;
import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.FIRST_NAME_FIELD_NAME;
import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.LAST_NAME_FIELD_NAME;
import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.PASSWORD_FIELD_NAME;
import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.PATRONYMIC_FIELD_NAME;
import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.PHONE_NUMBER_FIELD_NAME;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.NOT_EMPTY_FIELD_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.USER_EXISTS_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.WRONG_ADDRESS_FORMAT_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.WRONG_EMAIL_FORMAT_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.WRONG_FIRST_NAME_FORMAT_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.WRONG_LAST_NAME_FORMAT_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.WRONG_PASSWORD_FORMAT_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.WRONG_PATRONYMIC_FORMAT_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.WRONG_PHONE_NUMBER_FORMAT_MESSAGE_PROPERTY;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.ADDRESS_MAX_LENGTH;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.EMAIL_REGEX_PATTERN;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.FIRST_NAME_REGEX_PATTERN;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.LAST_NAME_AND_PATRONYMIC_REGEX_PATTERN;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.PASSWORD_REGEX_PATTERN;
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
                } catch (UserNotFoundModuleException e) {
                    checkPassword(errors, user);
                    checkFirstName(errors, user);
                    checkLastName(errors, user);
                    checkPatronymic(errors, user);
                    checkAddress(errors, user);
                    checkPhoneNumber(errors, user);
                } catch (UserDeletedModuleException e) {
                    errors.rejectValue(EMAIL_FIELD_NAME, USER_EXISTS_MESSAGE_PROPERTY);
                }
            } else {
                errors.rejectValue(EMAIL_FIELD_NAME, WRONG_EMAIL_FORMAT_MESSAGE_PROPERTY);
            }
        }
    }

    private void checkPhoneNumber(Errors errors, UserDTO user) {
        String phoneNumber = user.getPhoneNumber();
        if (StringUtils.isNotBlank(phoneNumber) && !phoneNumber.matches(PHONE_NUMBER_REGEXP_PATTERN)) {
            errors.rejectValue(PHONE_NUMBER_FIELD_NAME, WRONG_PHONE_NUMBER_FORMAT_MESSAGE_PROPERTY);
        }
    }

    private void checkAddress(Errors errors, UserDTO user) {
        String address = user.getAddress();
        if (StringUtils.isNotBlank(address) && address.length() > ADDRESS_MAX_LENGTH) {
            errors.rejectValue(ADDRESS_FIELD_NAME, WRONG_ADDRESS_FORMAT_MESSAGE_PROPERTY);
        }
    }

    private void checkPatronymic(Errors errors, UserDTO user) {
        String patronymic = user.getPatronymic();
        if (StringUtils.isNotBlank(patronymic) && !patronymic.matches(LAST_NAME_AND_PATRONYMIC_REGEX_PATTERN)) {
            errors.rejectValue(PATRONYMIC_FIELD_NAME, WRONG_PATRONYMIC_FORMAT_MESSAGE_PROPERTY);
        }
    }

    private void checkLastName(Errors errors, UserDTO user) {
        String lastName = user.getLastName();
        if (StringUtils.isNotBlank(lastName) && !lastName.matches(LAST_NAME_AND_PATRONYMIC_REGEX_PATTERN)) {
            errors.rejectValue(LAST_NAME_FIELD_NAME, WRONG_LAST_NAME_FORMAT_MESSAGE_PROPERTY);
        }
    }

    private void checkFirstName(Errors errors, UserDTO user) {
        String firstName = user.getFirstName();
        if (StringUtils.isNotBlank(firstName) && !firstName.matches(FIRST_NAME_REGEX_PATTERN)) {
            errors.rejectValue(FIRST_NAME_FIELD_NAME, WRONG_FIRST_NAME_FORMAT_MESSAGE_PROPERTY);
        }
    }

    private void checkPassword(Errors errors, UserDTO user) {
        String password = user.getPassword();
        if (StringUtils.isNotBlank(password) && !password.matches(PASSWORD_REGEX_PATTERN)) {
            errors.rejectValue(PASSWORD_FIELD_NAME, WRONG_PASSWORD_FORMAT_MESSAGE_PROPERTY);
        }
    }
}