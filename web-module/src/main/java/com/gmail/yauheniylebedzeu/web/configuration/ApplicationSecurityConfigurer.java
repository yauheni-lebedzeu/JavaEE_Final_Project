package com.gmail.yauheniylebedzeu.web.configuration;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.web.configuration.handler.AppAccessHandler;
import com.gmail.yauheniylebedzeu.web.configuration.handler.LoginAccessDeniedHandler;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Profile;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ACCESS_DENIED_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ACTUATOR_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ADMIN_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.API_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ARTICLES_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.CUSTOMER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.ITEMS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.MAIN_PAGE_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.REGISTER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.REVIEWS_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.SELLER_CONTROLLER_URL;
import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.USERS_CONTROLLER_URL;

@Configuration
@Profile("!test")
public class ApplicationSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Lazy
    public ApplicationSecurityConfigurer(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ADMIN_CONTROLLER_URL + "/**", ACTUATOR_CONTROLLER_URL + "/**")
                .hasAuthority(RoleDTOEnum.ADMIN.name())
                .antMatchers(CUSTOMER_CONTROLLER_URL + "/**")
                .hasAuthority(RoleDTOEnum.CUSTOMER_USER.name())
                .antMatchers(SELLER_CONTROLLER_URL + "/**")
                .hasAuthority(RoleDTOEnum.SALE_USER.name())
                .antMatchers(ARTICLES_CONTROLLER_URL + "/**", ITEMS_CONTROLLER_URL + "/**")
                .hasAnyAuthority(RoleDTOEnum.CUSTOMER_USER.name(), RoleDTOEnum.SALE_USER.name())
                .antMatchers(MAIN_PAGE_CONTROLLER_URL, REVIEWS_CONTROLLER_URL, ACCESS_DENIED_CONTROLLER_URL,
                        USERS_CONTROLLER_URL + REGISTER_CONTROLLER_URL, "/login*")
                .permitAll()
                .and()
                .formLogin()
                .loginPage("/login.html")
                .loginProcessingUrl("/login-check")
                .failureUrl("/login?error=true")
                .successHandler(new AppAccessHandler())
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new LoginAccessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    @AllArgsConstructor
    public static class ApplicationApiSecurityConfigurer extends WebSecurityConfigurerAdapter {

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
}