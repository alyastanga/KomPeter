/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.dao.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.dto.user.SessionDto;

public interface SessionDao {
    int createSession(@NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _userId,
            @NotNull String sessionToken) throws SQLException, IOException;

    int createSession(@NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _userId,
            @NotNull String sessionToken, String ipAddress) throws SQLException, IOException;

    Optional<SessionDto> getSessionById(@NotNull Connection conn,
            @Range(from = 0, to = Integer.MAX_VALUE) int _sessionId) throws IOException, SQLException;

    Optional<SessionDto> getSessionByToken(@NotNull Connection conn, @NotNull String sessionToken)
            throws IOException, SQLException;

    Optional<SessionDto> getSessionByUserId(@NotNull Connection conn,
            @Range(from = 0, to = Integer.MAX_VALUE) int _userId) throws IOException, SQLException;

    void removeSessionByToken(@NotNull Connection conn, @NotNull String sessionToken) throws IOException, SQLException;

    boolean sessionExists(@NotNull Connection conn, @NotNull String sessionToken) throws IOException, SQLException;
}
