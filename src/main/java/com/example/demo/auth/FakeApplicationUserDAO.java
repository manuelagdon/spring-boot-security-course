package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.example.demo.security.ApplicationUserRole.*;

@Repository("fake")
public class FakeApplicationUserDAO implements ApplicationUserDAO {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private FakeApplicationUserDAO(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<ApplicationUser> getApplicationUserByUsername(String username) {
        return getApplicationUsers().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> applicationUsers = new ArrayList<>(Arrays.asList(
                new ApplicationUser(
                        STUDENT.getGrantedAuthorities(),
                        "user",
                        passwordEncoder.encode("pass"),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        ADMIN.getGrantedAuthorities(),
                        "admin",
                        passwordEncoder.encode("pass"),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        ADMIN_TRAINEE.getGrantedAuthorities(),
                        "trainee",
                        passwordEncoder.encode("pass"),
                        true,
                         true,
                        true,
                        true
                )
        ));
        return applicationUsers;
    }
}
