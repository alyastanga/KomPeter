/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.user;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.NamedPreparedStatement;
import com.github.ragudos.kompeter.database.dao.user.UserRoleDao;
import com.github.ragudos.kompeter.database.sqlite.SqliteQueryLoader;
import com.github.ragudos.kompeter.utilities.StringUtils;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public class SqliteUserRoleDao implements UserRoleDao {
    @Override
    public Optional<String[]> getRolesOfUserById(
            @NotNull Connection conn, @Range(from = 0, to = 2147483647) int _userId)
            throws IOException, SQLException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_roles_by_user_id", "user_roles", SqlQueryType.SELECT))) {
            stmnt.setInt(1, _userId);

            ResultSet rs = stmnt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            String roles = rs.getString("user_roles");

            if (roles == null) {
                return Optional.empty();
            }

            return Optional.of(StringUtils.splitTrim(roles, ","));
        }
    }

    @Override
    public void removeRoleOfUser(
            @NotNull Connection conn, @NotNull String name, @Range(from = 0, to = 2147483647) int _userId)
            throws IOException, SQLException {
        try (NamedPreparedStatement stmnt =
                new NamedPreparedStatement(
                        conn,
                        SqliteQueryLoader.getInstance()
                                .get("delete_role_of_user", "user_roles", SqlQueryType.DELETE))) {
            stmnt.setString("name", name);
            stmnt.setInt("_user_id", _userId);

            stmnt.executeUpdate();
        }
    }
}
