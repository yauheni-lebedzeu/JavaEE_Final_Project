package com.gmail.yauheniylebedzeu.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccessDeniedController {

    @GetMapping(value = "/access-denied")
    public String getDeniedPage() {
        return "denied";
    }
}

