package com.gmail.yauheniylebedzeu.web.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;

@Profile("test")
@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
public class TestApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                .antMatchers(API_CONTROLLER_URL + "/**");
    }
}
