package com.github.ragudos.kompeter.database.dao.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import com.github.ragudos.kompeter.database.dto.user.SessionDto;

public interface SessionDao {
    int createSession(@NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _userId)
            throws SQLException, IOException;

    int createSession(
            @NotNull Connection conn,
            @Range(from = 0, to = Integer.MAX_VALUE) int _userId,
            String ipAddress)
            throws SQLException, IOException;

    public Optional<SessionDto> getSessionById(
            @NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _sessionId)
            throws IOException, SQLException;

    public Optional<SessionDto> getSessionByToken(
            @NotNull Connection conn, @NotNull String sessionToken) throws IOException, SQLException;

    public Optional<SessionDto> getSessionByUserId(
            @NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _userId)
            throws IOException, SQLException;

    public void removeSessionByToken(@NotNull Connection conn, @NotNull String sessionToken)
            throws IOException, SQLException;

    public boolean sessionExists(@NotNull Connection conn, @NotNull String sessionToken)
            throws IOException, SQLException;
}
