package com.gmail.yauheniylebedzeu.web.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.REVIEWS_CONTROLLER_URL;

@Controller
public class StartPageController {

    @GetMapping("/")
    public String getStartPage() {
        return "redirect:" + REVIEWS_CONTROLLER_URL;
    }
    
}
