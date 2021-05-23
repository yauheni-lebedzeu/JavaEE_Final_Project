package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorsDTO {

    Map<String, List<String>> errors = new HashMap<>();

    public void addError(String fieldName, List<String> errorMessage) {
        errors.put(fieldName, errorMessage);
    }

    public boolean isContainError(String fieldName) {
        return errors.containsKey(fieldName);
    }

    public void addErrorMessage(String fieldName, String errorMessage) {
        List<String> messages = errors.get(fieldName);
        messages.add(errorMessage);
    }
}