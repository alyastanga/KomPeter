package com.github.ragudos.kompeter.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.dto.UserMetadataDto;

public record Session(@NotNull UserMetadataDto user, @NotNull String token, @NotNull Timestamp expiresAt,
        String ipAddress) {
    public boolean isExpired() {
        return expiresAt.before(Timestamp.valueOf(LocalDateTime.now()));
    }
}
