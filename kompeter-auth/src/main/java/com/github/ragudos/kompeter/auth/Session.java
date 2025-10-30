/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.auth;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.jetbrains.annotations.NotNull;

import com.github.ragudos.kompeter.database.dto.user.UserMetadataDto;

public class Session {
    public static boolean isExpired(final @NotNull Timestamp ts) {
        return ts.before(Timestamp.valueOf(LocalDateTime.now(ZoneOffset.UTC)));
    }

    private final @NotNull Timestamp expiresAt;
    private final String ipAddress;
    private final @NotNull String sessionToken;

    private @NotNull UserMetadataDto user;

    public Session(@NotNull UserMetadataDto user, @NotNull String sessionToken, @NotNull Timestamp expiresAt,
            String ipAddress) {
        this.user = user;
        this.sessionToken = sessionToken;
        this.expiresAt = expiresAt;
        this.ipAddress = ipAddress;
    }

    public @NotNull Timestamp expiresAt() {
        return expiresAt;
    }

    public String ipAddress() {
        return ipAddress;
    }

    public boolean isExpired() {
        return isExpired(expiresAt);
    }

    public @NotNull String sessionToken() {
        return sessionToken;
    }

    public @NotNull UserMetadataDto user() {
        return user;
    }
}
