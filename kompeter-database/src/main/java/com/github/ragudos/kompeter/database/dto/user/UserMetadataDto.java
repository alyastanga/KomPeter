/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dto.user;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class UserMetadataDto {

    private final @Range(from = 0, to = Integer.MAX_VALUE) int _userId;
    private final @NotNull Timestamp _createdAt;
    private @NotNull String displayName;
    private @NotNull String firstName;
    private @NotNull String lastName;
    private @NotNull String[] roles;
    private @NotNull String email;

    public UserMetadataDto(
            @Range(from = 0, to = Integer.MAX_VALUE) int _userId,
            @NotNull Timestamp _createdAt,
            @NotNull String displayName,
            @NotNull String firstName,
            @NotNull String lastName,
            @NotNull String[] roles,
            @NotNull String email) {
        this._userId = _userId;
        this._createdAt = _createdAt;
        this.displayName = displayName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roles = roles;
        this.email = email;
    }

    public @Range(from = 0, to = Integer.MAX_VALUE) int _userId() {
        return _userId;
    }

    public @NotNull Timestamp _createdAt() {
        return _createdAt;
    }

    public @NotNull String displayName() {
        return displayName;
    }

    public @NotNull String firstName() {
        return firstName;
    }

    public @NotNull String lastName() {
        return lastName;
    }

    public @NotNull String[] roles() {
        return roles;
    }

    public @NotNull String email() {
        return email;
    }

    public boolean isRoleLess() {
        return roles.length == 0;
    }

    public boolean isAdmin() {
        for (String role : roles) {
            if (role.equals("admin")) {
                return true;
            }
        }

        return false;
    }

    public boolean isClerk() {
        for (String role : roles) {
            if (role.equals("clerk")) {
                return true;
            }
        }

        return false;
    }

    public boolean isLogistics() {
        for (String role : roles) {
            if (role.equals("logistics")) {
                return true;
            }
        }

        return false;
    }

    public boolean isPurchasingOfficer() {
        for (String role : roles) {
            if (role.equals("logistics")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        return String.format(
                "UserMetadataDto: _userId={}, _createdAt={}, displayName={}, firstName={}, lastName={},"
                        + " roles={}, email={}",
                _userId,
                _createdAt,
                displayName,
                firstName,
                lastName,
                roles,
                email);
    }
}
;
