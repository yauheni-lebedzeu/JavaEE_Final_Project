package com.gmail.yauheniylebedzeu.web.configuration;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import com.gmail.yauheniylebedzeu.web.configuration.handler.AppAccessHandler;
import com.gmail.yauheniylebedzeu.web.configuration.handler.LoginAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.gmail.yauheniylebedzeu.web.controller.constant.ControllerUrlConstant.*;

@Configuration
public class ApplicationSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;

    @Lazy
    public ApplicationSecurityConfigurer(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(ADMIN_CONTROLLER_URL + "/**")
                .hasAuthority(RoleDTOEnum.ADMIN.name())
                .antMatchers(CUSTOMER_CONTROLLER_URL + "/**")
                .hasAuthority(RoleDTOEnum.CUSTOMER_USER.name())
                .antMatchers( SELLER_CONTROLLER_URL + "/**")
                .hasAuthority(RoleDTOEnum.SALE_USER.name())
                .antMatchers(ARTICLES_CONTROLLER_URL + "/**", ITEMS_CONTROLLER_URL + "/**")
                .hasAnyAuthority(RoleDTOEnum.CUSTOMER_USER.name(), RoleDTOEnum.SALE_USER.name())
                .antMatchers(MAIN_PAGE_CONTROLLER_URL, REVIEWS_CONTROLLER_URL, ACCESS_DENIED_CONTROLLER_URL)
                .permitAll()
                .and()
                .formLogin()
                .successHandler(new AppAccessHandler())
                .and()
                .logout()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new LoginAccessDeniedHandler())
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

}
