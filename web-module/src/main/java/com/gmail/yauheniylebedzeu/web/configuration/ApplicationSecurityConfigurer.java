package com.gmail.yauheniylebedzeu.web.configuration;

import com.gmail.yauheniylebedzeu.web.configuration.handler.AppAccessHandler;
import com.gmail.yauheniylebedzeu.web.configuration.handler.LoginAccessDeniedHandler;
import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@AllArgsConstructor
public class ApplicationSecurityConfigurer extends WebSecurityConfigurerAdapter {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/users/**", "/reviews/admin/**")
                .hasAuthority(RoleDTOEnum.ADMIN.name())
                .antMatchers("/")
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

}
