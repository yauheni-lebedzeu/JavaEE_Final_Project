package com.gmail.yauheniylebedzeu.web.validator;

import com.gmail.yauheniylebedzeu.service.model.UserUpdateDTO;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.Objects;

import static com.gmail.yauheniylebedzeu.web.validator.constant.FieldNameConstant.*;
import static com.gmail.yauheniylebedzeu.web.validator.constant.MessagesPropertyConstant.*;
import static com.gmail.yauheniylebedzeu.web.validator.constant.ValidationConstant.*;

@Component
public class UserUpdateValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return UserUpdateDTO.class.equals(aClass);
    }


    @Override
    public void validate(Object object, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, FIRST_NAME_FIELD_NAME, NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, LAST_NAME_FIELD_NAME, NOT_EMPTY_FIELD_MESSAGE_PROPERTY);
        UserUpdateDTO userUpdate = (UserUpdateDTO) object;
        String password = userUpdate.getPassword();
        String reEnteredPassword = userUpdate.getReEnteredPassword();
        if (!Objects.isNull(password) && password.length() > 0) {
            if (!password.matches(PASSWORD_REGEX_PATTERN)) {
                errors.rejectValue(PASSWORD_FIELD_NAME, WRONG_PASSWORD_FORMAT_MESSAGE_PROPERTY);
            }
            if (!password.equals(reEnteredPassword)) {
                errors.rejectValue(RE_ENTERED_PASSWORD_FIELD_NAME, DON_T_MATCH_PASSWORDS_MESSAGE_PROPERTY);
            }
        }
        String firstName = userUpdate.getFirstName();
        if (StringUtils.isNotBlank(firstName) && !firstName.matches(FIRST_NAME_REGEX_PATTERN)) {
            errors.rejectValue(FIRST_NAME_FIELD_NAME, WRONG_FIRST_NAME_FORMAT_MESSAGE_PROPERTY);
        }
        String lastName = userUpdate.getLastName();
        if (StringUtils.isNotBlank(lastName) && !lastName.matches(LAST_NAME_AND_PATRONYMIC_REGEX_PATTERN)) {
            errors.rejectValue(LAST_NAME_FIELD_NAME, WRONG_LAST_NAME_FORMAT_MESSAGE_PROPERTY);
        }
        String address = userUpdate.getAddress();
        if (StringUtils.isNotBlank(address) && address.length() > ADDRESS_MAX_LENGTH) {
            errors.rejectValue(ADDRESS_FIELD_NAME, WRONG_ADDRESS_FORMAT_MESSAGE_PROPERTY);
        }
        String phoneNumber = userUpdate.getPhoneNumber();
        if (StringUtils.isNotBlank(phoneNumber) && !phoneNumber.matches(PHONE_NUMBER_REGEXP_PATTERN)) {
            errors.rejectValue(PHONE_NUMBER_FIELD_NAME, WRONG_PHONE_NUMBER_FORMAT_MESSAGE_PROPERTY);
        }
    }
}