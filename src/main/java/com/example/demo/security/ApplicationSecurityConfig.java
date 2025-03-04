package com.example.demo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.example.demo.security.ApplicationUserPermission.COURSE_WRITE;
import static com.example.demo.security.ApplicationUserRole.*;
import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) //Enable preAuthorize annotation
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
                    .antMatchers("/api/v1/**").hasRole(STUDENT.name()) // endpoints with api require authentication
                    //Ordering of ant matchers matter. Be careful of generic matchers and placing them above specific ones
                    .anyRequest().authenticated() // All other endpoints require authentication

                ) //antMatcher for springboot 2.7.x, requestMatcher for springboot 3.0
//                .csrf().disable() //Will explain in next section // need to pass CSRF token if csrf is enabled
                .csrf((csrf) -> csrf
                    .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()) //show xsrf token by default
                )
//                .httpBasic(withDefaults()) //Basic Auth
//                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .formLogin() // Form Authorization
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/courses", true)


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