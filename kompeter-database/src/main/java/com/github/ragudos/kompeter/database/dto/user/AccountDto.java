package com.github.ragudos.kompeter.database.dto.user;

import java.sql.Timestamp;
import org.jetbrains.annotations.NotNull;

public record AccountDto(
        int _accountId,
        @NotNull Timestamp _createdAt,
        int _userId,
        @NotNull String passwordHash,
        @NotNull String passwordSalt,
        @NotNull String email) {
    public static record AccountPassword(
            @NotNull String passwordHash, @NotNull String passwordSalt) {}
}
