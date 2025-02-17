package com.example.demo.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {
    // Define a SecurityFilterChain bean for HTTP security configurations
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
                new SecurityContextServerLogoutHandler(), new WebSessionServerLogoutHandler()
        );

        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated() // Require authentication for all requests
                )
                .httpBasic(httpBasic -> {
                })
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
        ; // Configure HTTP Basic authentication
        return http.build();
    }
}