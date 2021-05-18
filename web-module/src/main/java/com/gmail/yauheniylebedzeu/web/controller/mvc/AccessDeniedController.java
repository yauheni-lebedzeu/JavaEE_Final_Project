package com.gmail.yauheniylebedzeu.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ACCESS_DENIED_CONTROLLER_URL;

@Controller
public class AccessDeniedController {

    @GetMapping(value = ACCESS_DENIED_CONTROLLER_URL)
    public String getDeniedPage() {
        return "denied";
    }
}