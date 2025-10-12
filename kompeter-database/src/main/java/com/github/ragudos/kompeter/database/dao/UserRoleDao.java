package com.github.ragudos.kompeter.database.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

public interface UserRoleDao {
    Optional<String[]> getRolesOfUserById(
            @NotNull Connection conn, @Range(from = 0, to = Integer.MAX_VALUE) int _userId)
            throws IOException, SQLException;

    void removeRoleOfUser(
            @NotNull Connection conn,
            @NotNull String name,
            @Range(from = 0, to = Integer.MAX_VALUE) int _userId)
            throws IOException, SQLException;
}
