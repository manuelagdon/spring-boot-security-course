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

import static com.example.demo.security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.demo.security.ApplicationUserRole.*;
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
                        .antMatchers("/api/v2/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.DELETE, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                        .antMatchers(HttpMethod.POST, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                        .antMatchers(HttpMethod.PUT, "/management/api/**").hasAuthority(COURSE_WRITE.getPermission())
                        .antMatchers(HttpMethod.GET, "/management/api/**").hasAnyRole(ADMIN.name(), ADMIN_TRAINEE.name())
                        .anyRequest().authenticated() // All other endpoints require authentication

                ) //antMatcher for springboot 2.7.x, requestMatcher for springboot 3.0
                .csrf().disable() //Will explain in next section
                .httpBasic(withDefaults())
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))


        ; // Configure HTTP Basic authentication

        return http.build();
    }

    @Bean
    public InMemoryUserDetailsManager userDetailsService() {
        UserDetails user1 = User.builder()
                .username("user")
                .password(passwordEncoder.encode("pass"))
                .roles(ApplicationUserRole.STUDENT.name())
                .authorities(STUDENT.getGrantedAuthorities())
                .build();

        UserDetails user2 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("pass"))
                .roles(ADMIN.name())
                .authorities(ADMIN.getGrantedAuthorities())
                .build();

        UserDetails user3 = User.builder()
                .username("trainee")
                .password(passwordEncoder.encode("pass"))
                .roles(ApplicationUserRole.ADMIN_TRAINEE.name())
                .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
                .build();

        return new InMemoryUserDetailsManager(user1, user2, user3);
    }
}