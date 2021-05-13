package com.gmail.yauheniylebedzeu.web.controller.mvc;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.service.model.UserDTO;
import com.gmail.yauheniylebedzeu.service.model.UserLogin;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import static com.gmail.yauheniylebedzeu.web.controller.constant.AttributeNameConstant.ROLE_ATTRIBUTE_NAME;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.MAIN_PAGE_CONTROLLER_URL;

@Controller
public class MainPageController {

    @GetMapping(value = MAIN_PAGE_CONTROLLER_URL)
    public String getMainPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserLogin userLogin = (UserLogin) authentication.getPrincipal();
            UserDTO user = userLogin.getUser();
            RoleDTOEnum role = user.getRole();
            String roleName = role.name();
            model.addAttribute(ROLE_ATTRIBUTE_NAME, roleName);
        }
        return "main";
    }

}
