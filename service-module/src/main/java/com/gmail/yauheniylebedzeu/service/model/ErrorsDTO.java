package com.gmail.yauheniylebedzeu.service.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class ErrorsDTO {

    Map<String, List<String>> errors = new HashMap<>();

    public void addErrorMessage(String fieldName, String errorMessage) {
        if (errors.containsKey(fieldName)) {
            List<String> messages = errors.get(fieldName);
            messages.add(errorMessage);
        } else {
            ArrayList<String> errorMessages = new ArrayList<>();
            errorMessages.add(errorMessage);
            errors.put(fieldName, errorMessages);
        }
    }
}