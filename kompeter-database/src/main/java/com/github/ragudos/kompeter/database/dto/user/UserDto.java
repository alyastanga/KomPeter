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

public record UserDto(
        @Range(from = 0, to = Integer.MAX_VALUE) int _userId,
        @NotNull Timestamp _createdAt,
        @NotNull String displayName,
        @NotNull String firstName,
        @NotNull String lastName) {}
