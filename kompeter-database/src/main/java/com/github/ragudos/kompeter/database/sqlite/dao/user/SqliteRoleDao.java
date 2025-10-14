package com.github.ragudos.kompeter.database.sqlite.dao.user;

import com.github.ragudos.kompeter.database.dao.user.RoleDao;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;

public class SqliteRoleDao implements RoleDao {
    @Override
    public void addRole(@NotNull Connection conn, @NotNull String name)
            throws IOException, SQLException {
        throw new UnsupportedOperationException("Unimplemented method 'addRole'");
    }

    @Override
    public void addRole(@NotNull Connection conn, @NotNull String name, String description)
            throws IOException, SQLException {
        throw new UnsupportedOperationException("Unimplemented method 'addRole'");
    }
}
