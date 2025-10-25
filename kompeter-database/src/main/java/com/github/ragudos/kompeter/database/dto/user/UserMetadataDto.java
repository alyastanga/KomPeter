/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.user;

import java.sql.Timestamp;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class UserMetadataDto {

    private final @NotNull Timestamp _createdAt;
    private final @Range(from = 0, to = Integer.MAX_VALUE) int _userId;
    private @NotNull String displayName;
    private @NotNull String email;
    private @NotNull String firstName;
    private @NotNull String lastName;
    private @NotNull String[] roles;

    public UserMetadataDto(@Range(from = 0, to = Integer.MAX_VALUE) int _userId, @NotNull Timestamp _createdAt,
            @NotNull String displayName, @NotNull String firstName, @NotNull String lastName,
            @NotNull Optional<String[]> roles, @NotNull String email) {
        this._userId = _userId;
        this._createdAt = _createdAt;
        this.displayName = displayName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles.orElse(new String[]{});
        this.email = email;
    }

    public @NotNull Timestamp _createdAt() {
        return _createdAt;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int _userId() {
        return _userId;
    }

    public @NotNull String displayName() {
        return displayName;
    }

    public @NotNull String email() {
        return email;
    }

    public @NotNull String firstName() {
        return firstName;
    }

    public boolean isAdmin() {
        for (String role : roles) {
            if (role.equals("admin")) {
                return true;
            }
        }

        return false;
    }

    public boolean isAuditor() {
        for (String role : roles) {
            if (role.equals("auditor")) {
                return true;
            }

            return false;
        }

        return false;
    }

    public boolean isCashier() {
        for (String role : roles) {
            if (role.equals("cashier")) {
                return true;
            }
        }

        return false;
    }

    public boolean isInventoryClerk() {
        for (String role : roles) {
            if (role.equals("inventory clerk")) {
                return true;
            }
        }

        return false;
    }

    public boolean isManager() {
        for (String role : roles) {
            if (role.equals("manager")) {
                return true;
            }
        }

        return false;
    }

    public boolean isRoleLess() {
        return roles.length == 0;
    }

    public @NotNull String lastName() {
        return lastName;
    }

    public @NotNull String[] roles() {
        return roles;
    }

    @Override
    public String toString() {
        return String.format("UserMetadataDto: _userId={}, _createdAt={}, displayName={}, firstName={}, lastName={},"
                + " roles={}, email={}", _userId, _createdAt, displayName, firstName, lastName, roles, email);
    }
};
