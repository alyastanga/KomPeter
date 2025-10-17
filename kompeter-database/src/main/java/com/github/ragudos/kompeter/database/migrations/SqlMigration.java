/*
*
* MIT License
* Authors: Aaron Ragudos, Peter Dela Cruz, Hanz Mapua, Jerick Remo
* (C) 2025
*
*/
package com.github.ragudos.kompeter.database.migrations;

import org.jetbrains.annotations.NotNull;

public record SqlMigration(@NotNull String fileName, @NotNull String query) {
    public static record ParsedSqlMigration(
            int versionNumber, @NotNull String name, @NotNull String query) {}
}
