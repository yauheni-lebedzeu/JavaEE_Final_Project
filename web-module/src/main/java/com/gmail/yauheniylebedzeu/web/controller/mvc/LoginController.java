package com.gmail.yauheniylebedzeu.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping(value = "/login")
    public String getLoginPage(@RequestParam(name = "error", defaultValue = "false") boolean error, Model model) {
        model.addAttribute("error", error);
        return "login";
    }
}
