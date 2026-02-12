package com.library.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@Configuration
public class InMemoryUserDetailsService {
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.withUsername("user")
                .password("{bcrypt}$2a$10$7Qw6Qw6Qw6Qw6Qw6Qw6QwOQw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6") // password: password
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$10$7Qw6Qw6Qw6Qw6Qw6Qw6QwOQw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6Qw6") // password: password
                .roles("ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user, admin);
    }
}
