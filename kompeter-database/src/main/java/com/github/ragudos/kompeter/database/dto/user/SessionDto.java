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

public record SessionDto(
        int _sessionId,
        @NotNull Timestamp _createdAt,
        @NotNull Timestamp expiresAt,
        int _userId,
        @NotNull String sessionToken,
        String ipAddress) {

    public static Object us;
}
