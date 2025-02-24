package com.example.demo.security;

import java.util.HashSet;
import java.util.Set;

import static com.example.demo.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {

    STUDENT(new HashSet()),
    ADMIN(new HashSet(Set.of(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)));

    private final HashSet<ApplicationUserPermission> permissions;

    ApplicationUserRole(HashSet<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }
}
