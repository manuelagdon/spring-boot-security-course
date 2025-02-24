package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig {
    // Define a SecurityFilterChain bean for HTTP security configurations

    private final PasswordEncoder passwordEncoder;
    @Autowired
    public ApplicationSecurityConfig(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(authorize -> authorize
                        .antMatchers("/", "/index*", "/css/*", "/js/*").permitAll()  // Allow public access to endpoints under /public
                        .antMatchers("/api/v1/**").permitAll() // endpoints with api require authentication
                        .antMatchers(HttpMethod.GET,"/api/v2/**").hasRole("ADMIN")
                        .anyRequest().authenticated() // All other endpoints require authentication

                ) //antMatcher for springboot 2.7.x, requestMatcher for springboot 3.0
                .httpBasic(withDefaults())
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))


        ; // Configure HTTP Basic authentication

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.builder()
            .username("user1")
            .password(passwordEncoder.encode("pass1"))
            .roles(ApplicationUserRole.STUDENT.name())
            .build();

        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder.encode("pass2"))
                .roles(ApplicationUserRole.ADMIN.name())
                .build();

        return new InMemoryUserDetailsManager(user1, user2);
    }
}