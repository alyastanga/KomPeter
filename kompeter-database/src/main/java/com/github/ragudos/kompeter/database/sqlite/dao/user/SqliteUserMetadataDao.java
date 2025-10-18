/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.sqlite.dao.user;

import com.github.ragudos.kompeter.database.AbstractSqlQueryLoader.SqlQueryType;
import com.github.ragudos.kompeter.database.dao.user.UserMetadataDao;
import com.github.ragudos.kompeter.database.dto.user.UserMetadataDto;
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

public class SqliteUserMetadataDao implements UserMetadataDao {
    @Override
    public Optional<UserMetadataDto> getUserMetadata(
            @NotNull Connection conn, @Range(from = 0, to = 2147483647) int _userId)
            throws SQLException, IOException {
        try (PreparedStatement stmnt =
                conn.prepareStatement(
                        SqliteQueryLoader.getInstance()
                                .get("select_user_by_id", "user_metadata", SqlQueryType.SELECT))) {
            stmnt.setInt(1, _userId);

            ResultSet rs = stmnt.executeQuery();

            if (!rs.next()) {
                return Optional.empty();
            }

            String roles = rs.getString("roles");

            if (roles == null) {
                return Optional.empty();
            }

            return Optional.of(
                    new UserMetadataDto(
                            _userId,
                            rs.getTimestamp("_created_at"),
                            rs.getString("display_name"),
                            rs.getString("first_name"),
                            rs.getString("last_name"),
                            Optional.of(StringUtils.splitTrim(roles, ",")),
                            rs.getString("email")));
        }
    }
}
