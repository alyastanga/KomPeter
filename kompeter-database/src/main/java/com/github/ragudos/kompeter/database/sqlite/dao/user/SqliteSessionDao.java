/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.user;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.dao.user.SessionDao;
import com.github.ragudos.kompeter.database.dto.user.SessionDto;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class SqliteSessionDao implements SessionDao {
    @Override
    public int createSession(@NotNull Connection conn, @Range(from = 0, to = 2147483647) int _userId)
            throws SQLException, IOException {
        return createSession(conn, _userId, null);
    }

    @Override
    public int createSession(
            @NotNull Connection conn, @Range(from = 0, to = 2147483647) int _userId, String ipAddress)
            throws SQLException, IOException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance().get("create_session", "sessions", SqlQueryType.INSERT),
                        Statement.RETURN_GENERATED_KEYS)) {
            stmnt.setInt(1, _userId);
            stmnt.setString(2, ipAddress);
            stmnt.setString(3, "");

            stmnt.executeUpdate();

            ResultSet rs = stmnt.getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public Optional<SessionDto> getSessionById(
            @NotNull Connection conn, @Range(from = 0, to = 2147483647) int _sessionId)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_session_by_id", "sessions", SqlQueryType.SELECT))) {
            stmnt.setInt(1, _sessionId);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new SessionDto(
                                    rs.getInt("_session_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getTimestamp("expires_at"),
                                    rs.getInt("_user_id"),
                                    rs.getString("session_token"),
                                    rs.getString("ip_address")))
                    : Optional.empty();
        }
    }

    @Override
    public Optional<SessionDto> getSessionByToken(
            @NotNull Connection conn, @NotNull String sessionToken) throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_session_by_token", "sessions", SqlQueryType.SELECT))) {
            stmnt.setString(1, sessionToken);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new SessionDto(
                                    rs.getInt("_session_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getTimestamp("expires_at"),
                                    rs.getInt("_user_id"),
                                    rs.getString("session_token"),
                                    rs.getString("ip_address")))
                    : Optional.empty();
        }
    }

    @Override
    public Optional<SessionDto> getSessionByUserId(
            @NotNull Connection conn, @Range(from = 0, to = 2147483647) int _userId)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_session_by_user_id", "sessions", SqlQueryType.SELECT))) {
            stmnt.setInt(1, _userId);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new SessionDto(
                                    rs.getInt("_session_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getTimestamp("expires_at"),
                                    rs.getInt("_user_id"),
                                    rs.getString("session_token"),
                                    rs.getString("ip_address")))
                    : Optional.empty();
        }
    }

    @Override
    public void removeSessionByToken(@NotNull Connection conn, @NotNull String sessionToken)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("delete_session_by_token", "sessions", SqlQueryType.DELETE))) {
            stmnt.setString(1, sessionToken);

            stmnt.executeUpdate();
        }
    }

    @Override
    public boolean sessionExists(@NotNull Connection conn, @NotNull String sessionToken)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("session_exists", "sessions", SqlQueryType.SELECT))) {
            stmnt.setString(1, sessionToken);

            ResultSet rs = stmnt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }
}
