package com.gmail.yauheniylebedzeu.service.converter.impl;

import com.gmail.yauheniylebedzeu.service.converter.BindingResultConverter;
import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Locale;

@Component
@AllArgsConstructor
public class BindingResultConverterImpl implements BindingResultConverter {

    private final MessageSource messageSource;

    @Override
    public ErrorsDTO convertBindingResultToErrorsDTO(BindingResult bindingResult) {
        ErrorsDTO errors = new ErrorsDTO();
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        for (FieldError fieldError : fieldErrors) {
            String fieldName = fieldError.getField();
            String errorMessage = messageSource.getMessage(fieldError, Locale.ENGLISH);
            errors.addError(fieldName, errorMessage);
        }
        return errors;
    }
}