package com.github.ragudos.kompeter.database.dto;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public record UserMetadataDto(
        @Range(from = 0, to = Integer.MAX_VALUE) int _userId,
        @NotNull Timestamp _createdAt,
        @NotNull String displayName,
        @NotNull String firstName,
        @NotNull String lastName,
        @NotNull String[] roles) {}
;
