package com.gmail.yauheniylebedzeu.service.converter;

import com.gmail.yauheniylebedzeu.service.model.ErrorsDTO;
import org.springframework.validation.BindingResult;

public interface BindingResultConverter {

    ErrorsDTO convertBindingResultToErrorsDTO(BindingResult bindingResult);
}