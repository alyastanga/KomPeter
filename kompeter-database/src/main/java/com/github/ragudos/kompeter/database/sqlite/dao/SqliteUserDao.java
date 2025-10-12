package com.github.ragudos.kompeter.database.sqlite.dao;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.UserDao;
import com.github.ragudos.kompeter.database.dto.UserDto;
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

public class SqliteUserDao implements UserDao {
    @Override
    public int createUser(
            @NotNull Connection conn,
            @NotNull String displayName,
            @NotNull String firstName,
            @NotNull String lastName)
            throws IOException, SQLException {
        try (NamedPreparedStatement stmnt =
                new NamedPreparedStatement(
                        conn,
                        SqliteQueryLoader.getInstance().get("create_user", "users", SqlQueryType.INSERT),
                        Statement.RETURN_GENERATED_KEYS)) {
            stmnt.setString("display_name", displayName);
            stmnt.setString("first_name", firstName);
            stmnt.setString("last_name", lastName);

            stmnt.executeUpdate();

            ResultSet rs = stmnt.getPreparedStatement().getGeneratedKeys();

            return rs.next() ? rs.getInt(1) : -1;
        }
    }

    @Override
    public Optional<UserDto> getUserByDisplayName(
            @NotNull Connection conn, @NotNull String displayName) throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_user_by_display_name", "users", SqlQueryType.SELECT))) {
            stmnt.setString(1, displayName);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new UserDto(
                                    rs.getInt("_user_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getString("display_name"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name")))
                    : Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> getUserById(
            @NotNull Connection conn, @Range(from = 0, to = 2147483647) int _userId)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_user_by_id", "users", SqlQueryType.SELECT))) {
            stmnt.setInt(1, _userId);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new UserDto(
                                    _userId,
                                    rs.getTimestamp("_created_at"),
                                    rs.getString("display_name"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name")))
                    : Optional.empty();
        }
    }

    @Override
    public Optional<UserDto> getUserByEmail(@NotNull Connection conn, @NotNull String email)
            throws SQLException, IOException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_user_by_email", "users", SqlQueryType.SELECT))) {
            stmnt.setString(1, email);

            ResultSet rs = stmnt.executeQuery();

            return rs.next()
                    ? Optional.of(
                            new UserDto(
                                    rs.getInt("_user_id"),
                                    rs.getTimestamp("_created_at"),
                                    rs.getString("display_name"),
                                    rs.getString("first_name"),
                                    rs.getString("last_name")))
                    : Optional.empty();
        }
    }

    @Override
    public boolean displayNameTaken(@NotNull Connection conn, @NotNull String displayName)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_display_name_taken", "users", SqlQueryType.SELECT))) {
            stmnt.setString(1, displayName);

            ResultSet rs = stmnt.executeQuery();

            return rs.next() && rs.getInt(1) != 0;
        }
    }
}
