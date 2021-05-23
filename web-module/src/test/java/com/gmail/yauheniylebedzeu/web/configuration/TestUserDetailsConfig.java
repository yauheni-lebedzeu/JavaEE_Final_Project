package com.gmail.yauheniylebedzeu.web.configuration;

import com.gmail.yauheniylebedzeu.service.enums.RoleDTOEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;
import java.util.Collections;

@Profile("!it")
@Configuration
public class TestUserDetailsConfig {

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetails() {

        return new UserDetailsService() {

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                switch (username) {
                    case "user":
                        return getUserDetails();
                    case "admin":
                        return getAdminDetails();
                    default:
                        throw new UsernameNotFoundException(String.format("User with %s was not found", username));
                }
            }

            private UserDetails getAdminDetails() {
                return new UserDetails() {

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        String role = RoleDTOEnum.ADMIN.name();
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                        return Collections.singletonList(grantedAuthority);
                    }

                    @Override
                    public String getPassword() {
                        return encoder().encode("1234");
                    }

                    @Override
                    public String getUsername() {
                        return "admin";
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };
            }


            private UserDetails getUserDetails() {
                return new UserDetails() {

                    @Override
                    public Collection<? extends GrantedAuthority> getAuthorities() {
                        String role = RoleDTOEnum.SECURE_REST_API.name();
                        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role);
                        return Collections.singletonList(grantedAuthority);
                    }

                    @Override
                    public String getPassword() {
                        return encoder().encode("1234");
                    }

                    @Override
                    public String getUsername() {
                        return "user";
                    }

                    @Override
                    public boolean isAccountNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isAccountNonLocked() {
                        return true;
                    }

                    @Override
                    public boolean isCredentialsNonExpired() {
                        return true;
                    }

                    @Override
                    public boolean isEnabled() {
                        return true;
                    }
                };
            }
        };
    }
}