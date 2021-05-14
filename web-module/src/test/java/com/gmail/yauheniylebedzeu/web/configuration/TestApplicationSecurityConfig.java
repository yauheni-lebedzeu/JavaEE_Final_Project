package com.gmail.yauheniylebedzeu.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;

@Profile("test")
@Configuration
public class TestApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(API_CONTROLLER_URL + "/**");
    }
}
