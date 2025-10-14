package com.github.ragudos.kompeter.database.dao.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;

public interface RoleDao {
    void addRole(@NotNull Connection conn, @NotNull String name) throws IOException, SQLException;

    void addRole(@NotNull Connection conn, @NotNull String name, String description)
            throws IOException, SQLException;
}
