package com.gmail.yauheniylebedzeu.web.configuration;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;

@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
@Profile("!test")
@AllArgsConstructor
public class ApplicationApiSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher(API_CONTROLLER_URL + "/**")
                .authorizeRequests(a -> a.anyRequest().hasAnyAuthority(RoleDTOEnum.SECURE_REST_API.name()))
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }
}
