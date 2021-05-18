package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Optional;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.MAIN_PAGE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.util.ControllerUtil.getUserPrincipal;

@Controller
public class MainPageController {

    @GetMapping(value = MAIN_PAGE_CONTROLLER_URL)
    public String getMainPage(Model model) {
        Optional<UserDTO> optionalUser = getUserPrincipal();
        if (optionalUser.isPresent()) {
            UserDTO loggedInUser = optionalUser.get();
            RoleDTOEnum role = loggedInUser.getRole();
            String roleName = role.name();
            model.addAttribute("role", roleName);
        }
        return "main";
    }
}