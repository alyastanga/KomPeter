/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.migrations;

import java.sql.SQLException;
import org.jetbrains.annotations.NotNull;

public interface Migrator {
    /**
     * @return The combined sql queries of migrations as a whole. Useful if you want to initialize
     *     everything all at once and want to pipe this to a .sql file and use that in your workbench
     *     or sql server.
     */
    public @NotNull String getMigrationsAsAWhole();

    public void migrate() throws SQLException;
}
