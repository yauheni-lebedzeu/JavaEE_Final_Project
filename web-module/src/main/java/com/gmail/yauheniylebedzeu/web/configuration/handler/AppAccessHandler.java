package com.gmail.yauheniylebedzeu.web.configuration.handler;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.*;

public class AppAccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException, ServletException {

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();
        SimpleGrantedAuthority adminAuthority = new SimpleGrantedAuthority(RoleDTOEnum.ADMIN.name());
        SimpleGrantedAuthority customerAuthority = new SimpleGrantedAuthority(RoleDTOEnum.CUSTOMER_USER.name());
        if (roles.contains(adminAuthority)) {
            httpServletResponse.sendRedirect(ADMIN_CONTROLLER_URL + USERS_CONTROLLER_URL);
        } else if (roles.contains(customerAuthority)) {
            httpServletResponse.sendRedirect(CUSTOMER_CONTROLLER_URL + ARTICLES_CONTROLLER_URL);
        }
    }
}
