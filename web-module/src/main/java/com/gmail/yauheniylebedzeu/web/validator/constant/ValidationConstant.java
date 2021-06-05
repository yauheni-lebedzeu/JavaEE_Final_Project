package com.gmail.yauheniylebedzeu.web.validator.constant;

public interface ValidationConstant {
    String EMAIL_REGEX_PATTERN = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,4})$";
    String PASSWORD_REGEX_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~$^+=<>]).{8,20}$";
    String FIRST_NAME_REGEX_PATTERN = "^[A-Za-z]{1,20}$";
    String LAST_NAME_AND_PATRONYMIC_REGEX_PATTERN = "^[A-Za-z]{1,40}$";
    String PHONE_NUMBER_REGEXP_PATTERN = "^\\+375(\\s+)?\\(?(17|29|33|44)\\)?(\\s+)?[0-9]{3}-?[0-9]{2}-?[0-9]{2}$";
    int ADDRESS_MAX_LENGTH = 40;
}
