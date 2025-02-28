package com.example.demo.security;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {

    STUDENT(new HashSet()),
    ADMIN(new HashSet(Set.of(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE))),
    ADMIN_TRAINEE(new HashSet(Set.of(COURSE_READ, STUDENT_READ)));

    private final HashSet<ApplicationUserPermission> permissions;

    ApplicationUserRole(HashSet<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet()); //

        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name())); //add selected role as granted authority
        return permissions;
    }
}
